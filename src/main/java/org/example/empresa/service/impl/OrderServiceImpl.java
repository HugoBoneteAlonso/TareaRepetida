package org.example.empresa.service.impl;

import lombok.AllArgsConstructor;
import org.example.empresa.cache.CacheNames;
import org.example.empresa.dto.order.CreateOrderRequestDto;
import org.example.empresa.dto.order.OrderResponseDto;
import org.example.empresa.entity.*;
import org.example.empresa.exception.CustomerNotFoundException;
import org.example.empresa.exception.OrderNotFoundException;
import org.example.empresa.exception.ProductNotFoundException;
import org.example.empresa.mapper.OrderLineMapper;
import org.example.empresa.mapper.OrderMapper;
import org.example.empresa.repository.CustomerRepository;
import org.example.empresa.repository.OrderRepository;
import org.example.empresa.repository.ProductRepository;
import org.example.empresa.repository.specification.OrderSpecification;
import org.example.empresa.service.OrderService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository repository;
    private final OrderMapper mapper;
    private final OrderLineMapper orderLineMapper;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    @Override
    @Cacheable(value = CacheNames.ORDER_LIST)
    public List<OrderResponseDto> getAllWithDetails() {
        return repository.findAllWithDetails().stream().map(mapper::orderToOrderDto).toList();
    }

    @Override
    @Cacheable(value = CacheNames.ORDERS, key = "#id")
    public OrderResponseDto getByIdWithDetails(Long id) {
        return mapper.orderToOrderDto(repository.findById(id).orElseThrow(() -> new OrderNotFoundException(id)));
    }

    @Override
    @Caching(
        evict = {
            @CacheEvict(value = CacheNames.ORDER_LIST, allEntries = true),
            @CacheEvict(value = CacheNames.ORDER_SEARCH, allEntries = true)
    })
    public OrderResponseDto createOrder(CreateOrderRequestDto request) {
        Customer customer = customerRepository.findById(request.getCustomer())
                .orElseThrow(() -> new CustomerNotFoundException(request.getCustomer()));

        Order toCreate = mapper.toEntity(request);
        toCreate.setCustomer(customer);
        toCreate.setStatus(OrderStatus.PENDING);

        request.getLines().stream()
                .map(dto -> {
                    OrderLine line = orderLineMapper.toEntity(dto);
                    Product product = productRepository.findById(dto.getProduct())
                            .orElseThrow(() -> new ProductNotFoundException(dto.getProduct()));
                    line.setProduct(product);
                    line.setUnitPrice(product.getPrice());
                    return line;
                })
                .forEach(toCreate::addLine);
        Order saved = repository.save(toCreate);

        return mapper.orderToOrderDto(saved);
    }

    @Override
    @Caching (
        put = @CachePut(value = CacheNames.ORDERS, key = "#id"),
        evict = {
            @CacheEvict(value = CacheNames.ORDER_LIST, allEntries = true),
            @CacheEvict(value = CacheNames.ORDER_SEARCH, allEntries = true)
    })
    public OrderResponseDto updateOrderStatus(Long id, OrderStatus status) {
        Order toUpdate = repository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
        mapper.updateOrderStatus(status, toUpdate);
        repository.save(toUpdate);

        return mapper.orderToOrderDto(toUpdate);
    }

    @Override
    @Caching( evict = {
            @CacheEvict(value = CacheNames.ORDER_LIST, allEntries = true),
            @CacheEvict(value = CacheNames.ORDER_SEARCH, allEntries = true),
            @CacheEvict(value = CacheNames.ORDERS, key = "#id")
    })
    public void deleteOrder(Long id) {
        Order order = repository.findById(id).orElseThrow(()
                -> new OrderNotFoundException(id));

        if(order.getStatus() == OrderStatus.SHIPPED) {
            throw(new IllegalStateException("Can not cancel order with Shipped status"));
        }
        repository.delete(order);
    }

    @Override
    public List<OrderResponseDto> getAllOrdersByCustomer(Long id) {
        return repository.findAllByCustomerId(id).stream().map(mapper :: orderToOrderDto)
                .toList();
    }

    @Override
    public List<OrderResponseDto> getAllOrdersByStatus(String status) {
        return repository.findAllByStatus(OrderStatus.valueOf(status)).stream()
                .map(mapper::orderToOrderDto).toList();
    }

    @Override
    @Cacheable(value = CacheNames.ORDER_SEARCH,
            key =
                    "#pageable.pageNumber + '-' + " +
                            "#pageable.pageSize + '-' + " +
                            "#pageable.sort + '-' + " +
                            "#name + '-' + " +
                            "#status + '-' + " +
                            "#from + '-' + " +
                            "#to")
    public Page<OrderResponseDto> searchOrders(Pageable pageable, String name, OrderStatus status, LocalDateTime from, LocalDateTime to) {
        Specification<Order> spec = Specification.where(OrderSpecification.hasCustomerNameLike(name))
                .and(OrderSpecification.hasStatus(status))
                .and(OrderSpecification.createdBetween(from, to));

        return repository.findAll(spec, pageable).map(mapper::orderToOrderDto);
    }
}
package org.example.products.service.impl;

import lombok.AllArgsConstructor;
import org.example.products.dto.order.CreateOrderRequestDto;
import org.example.products.dto.order.OrderResponseDto;
import org.example.products.entity.*;
import org.example.products.exception.CustomerNotFoundException;
import org.example.products.exception.OrderNotFoundException;
import org.example.products.exception.ProductNotFoundException;
import org.example.products.mapper.OrderLineMapper;
import org.example.products.mapper.OrderMapper;
import org.example.products.repository.CustomerRepository;
import org.example.products.repository.OrderRepository;
import org.example.products.repository.ProductRepository;
import org.example.products.service.OrderService;
import org.springframework.stereotype.Service;

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
    public List<OrderResponseDto> getAllWithDetails() {
        return repository.findAllWithDetails().stream().map(mapper::orderToOrderDto).toList();
    }

    @Override
    public OrderResponseDto getByIdWithDetails(Long id) {
        return mapper.orderToOrderDto(repository.findById(id).orElseThrow(() -> new OrderNotFoundException(id)));
    }

    @Override
    public OrderResponseDto createOrder(CreateOrderRequestDto request) {
        Customer customer = customerRepository.findById(request.getCustomer())
                .orElseThrow(() -> new CustomerNotFoundException(request.getCustomer()));

        Order toCreate = mapper.toEntity(request);
        toCreate.setCustomer(customer);

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
    public OrderResponseDto updateOrderStatus(Long id, OrderStatus status) {
        Order toUpdate = repository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
        mapper.updateOrderStatus(status, toUpdate);
        repository.save(toUpdate);

        return mapper.orderToOrderDto(toUpdate);
    }

    @Override
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
}

package org.example.products.service.impl;

import lombok.AllArgsConstructor;
import org.example.products.dto.customer.CustomerRequestDto;
import org.example.products.dto.customer.CustomerSummaryDto;
import org.example.products.dto.order.OrderResponseDto;
import org.example.products.entity.Customer;
import org.example.products.exception.CustomerNotFoundException;
import org.example.products.mapper.CustomerMapper;
import org.example.products.mapper.OrderMapper;
import org.example.products.repository.CustomerRepository;
import org.example.products.repository.OrderRepository;
import org.example.products.service.CustomerService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository repository;
    private final CustomerMapper mapper;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    public List<CustomerSummaryDto> getAlCustomers() {
        return repository.findAll().stream().map(this::customerDataUpdater).toList();
    }

    @Override
    public CustomerSummaryDto getByid(Long id) {
        Customer customer = repository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));

        return customerDataUpdater(customer);

    }

    @Override
    public CustomerSummaryDto createCustomer(CustomerRequestDto request) {
        Customer toCreate = mapper.toEntity(request);
        Customer saved = repository.save(toCreate);
        return customerDataUpdater(saved);
    }

    private CustomerSummaryDto customerDataUpdater(Customer data) {
        CustomerSummaryDto dto = mapper.customerToDto(data);
        List<OrderResponseDto> orders = orderRepository.findAllByCustomerId(data.getId())
                .stream().map(orderMapper::orderToOrderDto).toList();
        dto.setTotalOrders(orders.size());

        BigDecimal totalSpent = BigDecimal.ZERO;
        for(OrderResponseDto order : orders) {
            totalSpent = totalSpent.add(order.getTotalAmount());
        }

        dto.setTotalSpent(totalSpent);

        return dto;
    }
}

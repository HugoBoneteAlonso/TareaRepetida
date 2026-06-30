package org.example.products.service.impl;

import lombok.AllArgsConstructor;
import org.example.products.dto.customer.CustomerRequestDto;
import org.example.products.dto.customer.CustomerSummaryDto;
import org.example.products.entity.Customer;
import org.example.products.exception.CustomerNotFoundException;
import org.example.products.mapper.CustomerMapper;
import org.example.products.repository.CustomerRepository;
import org.example.products.service.CustomerService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository repository;
    private final CustomerMapper mapper;

    @Override
    public List<CustomerSummaryDto> getAlCustomers() {
        return repository.findAll().stream().map(mapper::customerToDto).toList();
    }

    @Override
    public CustomerSummaryDto getByid(Long id) {
        return mapper.customerToDto(repository.findById(id)
                .orElseThrow(() ->new CustomerNotFoundException(id)));
    }

    @Override
    public CustomerSummaryDto createCustomer(CustomerRequestDto request) {
        Customer toCreate = mapper.toEntity(request);
        Customer saved = repository.save(toCreate);
        return mapper.customerToDto(saved);
    }
}

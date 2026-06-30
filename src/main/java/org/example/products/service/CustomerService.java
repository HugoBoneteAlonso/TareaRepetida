package org.example.products.service;

import org.example.products.dto.customer.CustomerRequestDto;
import org.example.products.dto.customer.CustomerSummaryDto;

import java.util.List;

public interface CustomerService {
    List<CustomerSummaryDto> getAlCustomers();
    CustomerSummaryDto getByid(Long id);
    CustomerSummaryDto createCustomer(CustomerRequestDto request);
}

package org.example.empresa.service;

import org.example.empresa.dto.customer.CustomerRequestDto;
import org.example.empresa.dto.customer.CustomerSummaryDto;

import java.util.List;

public interface CustomerService {
    List<CustomerSummaryDto> getAlCustomers();
    CustomerSummaryDto getByid(Long id);
    CustomerSummaryDto createCustomer(CustomerRequestDto request);
}

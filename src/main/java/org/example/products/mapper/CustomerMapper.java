package org.example.products.mapper;

import org.example.products.dto.customer.CustomerRequestDto;
import org.example.products.dto.customer.CustomerSummaryDto;
import org.example.products.entity.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CustomerSummaryDto customerToDto(Customer customer);
    Customer toEntity(CustomerRequestDto dto);
}

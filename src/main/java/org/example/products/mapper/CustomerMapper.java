package org.example.products.mapper;

import org.example.products.dto.CustomerSummaryDto;
import org.example.products.entity.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CustomerSummaryDto customerToDto(Customer customer);
}

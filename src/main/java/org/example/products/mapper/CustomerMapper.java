package org.example.products.mapper;

import org.example.products.dto.customer.CustomerRequestDto;
import org.example.products.dto.customer.CustomerSummaryDto;
import org.example.products.entity.Customer;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CustomerSummaryDto customerToDto(Customer customer);
    Customer toEntity(CustomerRequestDto dto);

    @AfterMapping
    default void calculateSpentAndOrders(Customer source, @MappingTarget CustomerSummaryDto dto) {

    }
}

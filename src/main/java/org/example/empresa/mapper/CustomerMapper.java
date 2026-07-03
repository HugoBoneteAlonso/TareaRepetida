package org.example.empresa.mapper;

import org.example.empresa.dto.customer.CustomerRequestDto;
import org.example.empresa.dto.customer.CustomerSummaryDto;
import org.example.empresa.entity.Customer;
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

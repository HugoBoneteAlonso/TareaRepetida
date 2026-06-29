package org.example.products.mapper;

import org.example.products.dto.OrderLineResponseDto;
import org.example.products.entity.OrderLine;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface OrderLineMapper {
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "lineTotal", ignore = true)
    OrderLineResponseDto orderLineToDto(OrderLine orderLine);

    @AfterMapping
    default void calculateLineTotal(OrderLine source, @MappingTarget OrderLineResponseDto dto) {
        if(source.getUnitPrice() != null && source.getQuantity() != null) {
            dto.setLineTotal(source.getUnitPrice().multiply(BigDecimal.valueOf(source.getQuantity())));
        }
    }
}

package org.example.products.mapper;

import org.example.products.dto.CreateOrderRequestDto;
import org.example.products.dto.OrderLineResponseDto;
import org.example.products.dto.OrderResponseDto;
import org.example.products.entity.Order;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.math.BigDecimal;
import java.util.Objects;

@Mapper(componentModel = "spring", uses = {CustomerMapper.class, OrderLineMapper.class})
public interface OrderMapper {
    @Mapping(target = "totalAmount", ignore = true)
    OrderResponseDto orderToOrderDto(Order order);

    Order toEntity(CreateOrderRequestDto dto);

    @AfterMapping
    default void calculateTotalAmount(Order source, @MappingTarget OrderResponseDto dto) {
        if(dto.getLines() != null) {
            BigDecimal total = dto.getLines().stream().map(OrderLineResponseDto::getLineTotal)
                    .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
            dto.setTotalAmount(total);
        }
    }
}

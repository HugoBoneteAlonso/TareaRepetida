package org.example.products.mapper;

import org.example.products.dto.order.CreateOrderRequestDto;
import org.example.products.dto.order.OrderLineResponseDto;
import org.example.products.dto.order.OrderResponseDto;
import org.example.products.entity.Order;
import org.example.products.entity.OrderStatus;
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

    @Mapping(target = "customer", ignore = true)
    Order toEntity(CreateOrderRequestDto dto);
    @AfterMapping
    default void calculateTotalAmount(Order source, @MappingTarget OrderResponseDto dto) {
        if(dto.getLines() != null) {
            BigDecimal total = dto.getLines().stream().map(OrderLineResponseDto::getLineTotal)
                    .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
            dto.setTotalAmount(total);
        }
    }

    void updateOrderStatus(OrderStatus status, @MappingTarget Order order);
}

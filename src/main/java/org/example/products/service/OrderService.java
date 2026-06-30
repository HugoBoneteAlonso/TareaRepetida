package org.example.products.service;

import org.example.products.dto.order.CreateOrderRequestDto;
import org.example.products.dto.order.OrderResponseDto;
import org.example.products.entity.OrderStatus;

import java.util.List;

public interface OrderService {
    List<OrderResponseDto> getAllWithDetails();
    OrderResponseDto getByIdWithDetails(Long id);
    OrderResponseDto createOrder(CreateOrderRequestDto request);
    OrderResponseDto updateOrderStatus(Long id, OrderStatus status);
    void deleteOrder(Long id);
    List<OrderResponseDto> getAllOrdersByCustomer(Long id);

}

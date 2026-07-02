package org.example.products.service;

import org.example.products.dto.order.CreateOrderRequestDto;
import org.example.products.dto.order.OrderResponseDto;
import org.example.products.entity.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {
    List<OrderResponseDto> getAllWithDetails();
    OrderResponseDto getByIdWithDetails(Long id);
    OrderResponseDto createOrder(CreateOrderRequestDto request);
    OrderResponseDto updateOrderStatus(Long id, OrderStatus status);
    void deleteOrder(Long id);
    List<OrderResponseDto> getAllOrdersByCustomer(Long id);
    List<OrderResponseDto> getAllOrdersByStatus(String status);
    Page<OrderResponseDto> searchOrders(Pageable pageable, String name, OrderStatus status,
                                        LocalDateTime from, LocalDateTime to);

}

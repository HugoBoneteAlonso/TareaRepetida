package org.example.products.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.products.dto.order.CreateOrderRequestDto;
import org.example.products.dto.order.OrderResponseDto;
import org.example.products.entity.OrderStatus;
import org.example.products.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {
    private final OrderService service;

    @GetMapping
    public List<OrderResponseDto> getAllOrders() {
        return service.getAllWithDetails();
    }

    @GetMapping("/{id}")
    public OrderResponseDto getOrderById(@PathVariable Long id) {
        return service.getByIdWithDetails(id);
    }

    @PostMapping
    public OrderResponseDto createOrder(@RequestBody @Valid CreateOrderRequestDto request) {
        return service.createOrder(request);
    }

    @PutMapping("/{id}/status")
    public OrderResponseDto updateOrderStatus(@PathVariable Long id, @RequestParam OrderStatus status) {
        return service.updateOrderStatus(id, status);
    }

    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable Long id) {
        service.deleteOrder(id);
    }
}

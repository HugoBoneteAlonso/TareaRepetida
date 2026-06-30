package org.example.products.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.products.dto.customer.CustomerRequestDto;
import org.example.products.dto.customer.CustomerSummaryDto;
import org.example.products.dto.order.OrderResponseDto;
import org.example.products.service.CustomerService;
import org.example.products.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customers")
public class CustomerController {
    private final CustomerService service;
    private final OrderService orderService;

    @GetMapping
    public List<CustomerSummaryDto> getAllCustomers() {
        return service.getAlCustomers();
    }

    @GetMapping("/{id}")
    public CustomerSummaryDto getCustomerById(@PathVariable Long id) {
        return service.getByid(id);
    }

    @PostMapping
    public CustomerSummaryDto createCustomer(@RequestBody @Valid CustomerRequestDto request) {
        return service.createCustomer(request);
    }

    @GetMapping("/{id}/orders")
    public List<OrderResponseDto> getOrdersByCustomer(@PathVariable Long id) {
        return orderService.getAllOrdersByCustomer(id);
    }
}

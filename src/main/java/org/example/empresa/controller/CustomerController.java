package org.example.empresa.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.empresa.dto.customer.CustomerRequestDto;
import org.example.empresa.dto.customer.CustomerSummaryDto;
import org.example.empresa.dto.order.OrderResponseDto;
import org.example.empresa.service.CustomerService;
import org.example.empresa.service.OrderService;
import org.springframework.http.HttpStatus;
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
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerSummaryDto createCustomer(@RequestBody @Valid CustomerRequestDto request) {
        return service.createCustomer(request);
    }

    @GetMapping("/{id}/orders")
    public List<OrderResponseDto> getOrdersByCustomer(@PathVariable Long id) {
        return orderService.getAllOrdersByCustomer(id);
    }
}

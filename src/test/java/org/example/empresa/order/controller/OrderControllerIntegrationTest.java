package org.example.empresa.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.example.empresa.dto.order.CreateOrderLineRequestDto;
import org.example.empresa.dto.order.CreateOrderRequestDto;
import org.example.empresa.entity.*;
import org.example.empresa.repository.CategoryRepository;
import org.example.empresa.repository.CustomerRepository;
import org.example.empresa.repository.OrderRepository;
import org.example.empresa.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class OrderControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private OrderRepository repository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Product existingProduct;
    private Category existingCategory;
    private Customer existingCustomer;
    private Order existingOrder;

    @BeforeEach
    void setUp() {
        existingCustomer = customerRepository.save(new Customer(null, "Juan Perez",
                "juan@gmail.com", "600123123"));

        existingCategory = categoryRepository.save(new Category(null, "Electronica"));

        existingProduct = productRepository.save(
                new Product(null, "Mouse", "Mouse inalambrico", new BigDecimal("25.99"),
                        50, null, existingCategory));

        existingOrder = new Order(null, null, OrderStatus.PENDING, existingCustomer,
                new ArrayList<>(List.of(new OrderLine(null, null, existingProduct, 2, existingProduct.getPrice()),
                        new OrderLine(null, null, existingProduct, 2, existingProduct.getPrice()))));
        existingOrder = repository.save(existingOrder);
    }

    @Test
    void shouldCreateOrderWithMultipleLines() throws Exception {
        //Given
        CreateOrderLineRequestDto linesRequest = new CreateOrderLineRequestDto(existingProduct.getId(), 2);
        CreateOrderRequestDto request = new CreateOrderRequestDto(existingCustomer.getId(), List.of(linesRequest, linesRequest));

        //Then + When
        mockMvc.perform(post("/api/v1/orders").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.totalAmount").value(existingProduct.getPrice().multiply(BigDecimal.valueOf(4))));

    }

    @Test
    void shouldReturn404WhenCustomerNotFound() throws Exception {
        //Given
        CreateOrderLineRequestDto linesRequest = new CreateOrderLineRequestDto(existingProduct.getId(), 2);
        CreateOrderRequestDto request = new CreateOrderRequestDto(9999L, List.of(linesRequest, linesRequest));

        //Then + When
        mockMvc.perform(post("/api/v1/orders").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn404WhenProductInLineDoesNotExist() throws Exception {
        //Given
        CreateOrderLineRequestDto linesRequest = new CreateOrderLineRequestDto(9999L, 2);
        CreateOrderRequestDto request = new CreateOrderRequestDto(existingCustomer.getId(), List.of(linesRequest, linesRequest));

        //Then + When
        mockMvc.perform(post("/api/v1/orders").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn400WhenQuantityIsZero() throws Exception {
        //Given
        CreateOrderLineRequestDto linesRequest = new CreateOrderLineRequestDto(existingProduct.getId(), 0);
        CreateOrderRequestDto request = new CreateOrderRequestDto(existingCustomer.getId(), List.of(linesRequest, linesRequest));

        //Then + When
        mockMvc.perform(post("/api/v1/orders").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldFindOrderWithNestedCustomerAndLines() throws Exception{
        mockMvc.perform(get("/api/v1/orders/" + existingOrder.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customer").exists())
                .andExpect(jsonPath("$.lines").exists())
                .andExpect(jsonPath("$.lines.length()").value(2));
    }

    @Test
    void shouldUpdateOrderStatus() throws Exception {
        mockMvc.perform(put("/api/v1/orders/" + existingOrder.getId() + "/status?status=CONFIRMED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingOrder.getId()))
                .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }

    @Test
    void shouldFilterOrdersByStatusAndDateRange() throws Exception {
        mockMvc.perform(get("/api/v1/orders/search?status=" + existingOrder.getStatus() +
                "&from=2026-01-01T00:00:00&to=" + existingOrder.getOrderDate()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(existingOrder.getId()));
    }
}

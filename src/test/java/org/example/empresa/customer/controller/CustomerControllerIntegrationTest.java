package org.example.empresa.customer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.example.empresa.dto.customer.CustomerRequestDto;
import org.example.empresa.entity.Customer;
import org.example.empresa.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class CustomerControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private CustomerRepository repository;

    private Customer existingCustomer;

    @BeforeEach
    void setUp() {
        existingCustomer = repository.save(new Customer(null, "Juan Perez",
                "juan@gmail.com", "600123123"));
    }

    @Test
    void shouldGetAllCustomers() throws Exception {
        mockMvc.perform(get("/api/v1/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(greaterThan(1)));
    }

    @Test
    void shouldCreateCustomer() throws Exception {
        CustomerRequestDto request = new CustomerRequestDto("Juan",
                "juan123@gmail.com", "612123123");

        mockMvc.perform(post("/api/v1/customers").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value(request.getEmail()));
    }

    @Test
    void shouldGetCustomerById() throws Exception {
        mockMvc.perform(get("/api/v1/customers/" + existingCustomer.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingCustomer.getId()));
    }

    @Test
    void shouldReturn404WhenCustomerNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/customers/9999"))
                .andExpect(status().isNotFound());
    }
}

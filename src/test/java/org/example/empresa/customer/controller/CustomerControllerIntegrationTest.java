package org.example.empresa.customer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.example.empresa.dto.customer.CustomerRequestDto;
import org.example.empresa.dto.security.LoginRequestDto;
import org.example.empresa.entity.Customer;
import org.example.empresa.entity.Role;
import org.example.empresa.entity.User;
import org.example.empresa.repository.CustomerRepository;
import org.example.empresa.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    private Customer existingCustomer;

    @BeforeEach
    void setUp() {
        existingCustomer = repository.save(new Customer(null, "Juan Perez",
                "juan@gmail.com", "600123123"));

        User user2 = new User(2L, "user@gmail.com", passwordEncoder.encode("12345")
                , Role.USER, true);

        userRepository.save(user2);
    }

    private String loginAndGetToken(String email) throws Exception {
        LoginRequestDto requestDto = new LoginRequestDto(email, "12345");
        String response = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestDto))).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        return mapper.readTree(response).get("token").asText();
    }

    @Test
    void shouldGetAllCustomers() throws Exception {
        String token = loginAndGetToken("user@gmail.com");

        mockMvc.perform(get("/api/v1/customers").header("Authorization","Bearer "
                        + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(greaterThan(1)));
    }

    @Test
    void shouldCreateCustomer() throws Exception {
        String token = loginAndGetToken("user@gmail.com");

        CustomerRequestDto request = new CustomerRequestDto("Juan",
                "juan123@gmail.com", "612123123");

        mockMvc.perform(post("/api/v1/customers").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)).header("Authorization","Bearer "
                        + token))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value(request.getEmail()));
    }

    @Test
    void shouldGetCustomerById() throws Exception {
        String token = loginAndGetToken("user@gmail.com");

        mockMvc.perform(get("/api/v1/customers/" + existingCustomer.getId()).header("Authorization","Bearer "
                        + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingCustomer.getId()));
    }

    @Test
    void shouldReturn404WhenCustomerNotFound() throws Exception {
        String token = loginAndGetToken("user@gmail.com");

        mockMvc.perform(get("/api/v1/customers/9999")
                .header("Authorization","Bearer "
                        + token))
                .andExpect(status().isNotFound());
    }
}

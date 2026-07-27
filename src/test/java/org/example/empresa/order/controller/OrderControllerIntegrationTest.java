package org.example.empresa.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManagerFactory;
import jakarta.transaction.Transactional;
import org.example.empresa.dto.order.CreateOrderLineRequestDto;
import org.example.empresa.dto.order.CreateOrderRequestDto;
import org.example.empresa.dto.security.LoginRequestDto;
import org.example.empresa.entity.*;
import org.example.empresa.repository.*;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Product existingProduct;
    private Customer existingCustomer;
    private Order existingOrder;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        productRepository.deleteAll();
        customerRepository.deleteAll();

        existingCustomer = customerRepository.save(new Customer(null, "Juan Perez",
                "juan@gmail.com", "600123123"));

        Category existingCategory = categoryRepository.save(new Category(null, "Electronica"));

        existingProduct = productRepository.save(
                new Product(null, "Mouse", "Mouse inalambrico", new BigDecimal("25.99"),
                        50, null, existingCategory));

        existingOrder = new Order(null, null, OrderStatus.PENDING, existingCustomer,new ArrayList<>());

        OrderLine line1 = new OrderLine(null, existingOrder, existingProduct, 2, existingProduct.getPrice());
        OrderLine line2 = new OrderLine(null, existingOrder, existingProduct, 2, existingProduct.getPrice());

        existingOrder.addLine(line1);
        existingOrder.addLine(line2);

        existingOrder = repository.save(existingOrder);

        User user = new User(1L, "admin@gmail.com", passwordEncoder.encode("12345")
                , Role.ADMIN, true);

        userRepository.save(user);
    }

    private String loginAndGetToken(String email) throws Exception {
        LoginRequestDto requestDto = new LoginRequestDto(email, "12345");
        String response = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestDto))
                ).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        return mapper.readTree(response).get("token").asText();
    }

    @Test
    void shouldCreateOrderWithMultipleLines() throws Exception {
        String token = loginAndGetToken("admin@gmail.com");
        //Given
        CreateOrderLineRequestDto linesRequest = new CreateOrderLineRequestDto(existingProduct.getId(), 2);
        CreateOrderRequestDto request = new CreateOrderRequestDto(existingCustomer.getId(), List.of(linesRequest, linesRequest));

        //Then + When
        mockMvc.perform(post("/api/v1/orders").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request))
                        .header("Authorization","Bearer "
                                + token))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.totalAmount").value(existingProduct.getPrice().multiply(BigDecimal.valueOf(4))));

    }

    @Test
    void shouldReturn404WhenCustomerNotFound() throws Exception {
        String token = loginAndGetToken("admin@gmail.com");

        //Given
        CreateOrderLineRequestDto linesRequest = new CreateOrderLineRequestDto(existingProduct.getId(), 2);
        CreateOrderRequestDto request = new CreateOrderRequestDto(9999L, List.of(linesRequest, linesRequest));

        //Then + When
        mockMvc.perform(post("/api/v1/orders").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request))
                        .header("Authorization","Bearer "
                                + token))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn404WhenProductInLineDoesNotExist() throws Exception {
        String token = loginAndGetToken("admin@gmail.com");

        //Given
        CreateOrderLineRequestDto linesRequest = new CreateOrderLineRequestDto(9999L, 2);
        CreateOrderRequestDto request = new CreateOrderRequestDto(existingCustomer.getId(), List.of(linesRequest, linesRequest));

        //Then + When
        mockMvc.perform(post("/api/v1/orders").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request))
                        .header("Authorization","Bearer "
                                + token))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn400WhenQuantityIsZero() throws Exception {
        String token = loginAndGetToken("admin@gmail.com");

        //Given
        CreateOrderLineRequestDto linesRequest = new CreateOrderLineRequestDto(existingProduct.getId(), 0);
        CreateOrderRequestDto request = new CreateOrderRequestDto(existingCustomer.getId(), List.of(linesRequest, linesRequest));

        //Then + When
        mockMvc.perform(post("/api/v1/orders").contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization","Bearer "
                                + token)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldFindOrderWithNestedCustomerAndLines() throws Exception{
        String token = loginAndGetToken("admin@gmail.com");

        mockMvc.perform(get("/api/v1/orders/" + existingOrder.getId())
                        .header("Authorization","Bearer "
                                + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customer").exists())
                .andExpect(jsonPath("$.lines").exists())
                .andExpect(jsonPath("$.lines.length()").value(2));
    }

    @Test
    void shouldUpdateOrderStatus() throws Exception {
        String token = loginAndGetToken("admin@gmail.com");

        mockMvc.perform(put("/api/v1/orders/" + existingOrder.getId() + "/status?status=CONFIRMED")
                        .header("Authorization","Bearer "
                                + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingOrder.getId()))
                .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }

    @Test
    void shouldFilterOrdersByStatusAndDateRange() throws Exception {
        String token = loginAndGetToken("admin@gmail.com");

        mockMvc.perform(get("/api/v1/orders/search?status=" + existingOrder.getStatus() +
                "&from=2026-01-01T00:00:00&to=2029-01-01T00:00:00")
                        .header("Authorization","Bearer "
                                + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(existingOrder.getId()));
    }

    @Test
    void shouldGetAllOrders() throws Exception{
        String token = loginAndGetToken("admin@gmail.com");

        mockMvc.perform(get("/api/v1/orders")
                        .header("Authorization","Bearer "
                                + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(greaterThan(0)));
    }

    @Test
    void shouldGetAllOrdersByStatus() throws Exception{
        String token = loginAndGetToken("admin@gmail.com");

        mockMvc.perform(get("/api/v1/orders/status?status=PENDING")
                        .header("Authorization","Bearer "
                                + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(existingOrder.getId()));
    }

    @Test
    void shouldDeleteOrder() throws Exception {
        String token = loginAndGetToken("admin@gmail.com");

        mockMvc.perform(delete("/api/v1/orders/" + existingOrder.getId())
                        .header("Authorization","Bearer "
                                + token))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturn404WhenNotFoundOrder() throws Exception {
        String token = loginAndGetToken("admin@gmail.com");

        mockMvc.perform(delete("/api/v1/orders/9999")
                        .header("Authorization","Bearer "
                                + token))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn500WhenNotFoundOrder() throws Exception {
        String token = loginAndGetToken("admin@gmail.com");

        existingOrder.setStatus(OrderStatus.SHIPPED);
        mockMvc.perform(delete("/api/v1/orders/" + existingOrder.getId())
                        .header("Authorization","Bearer "
                        + token))
                .andExpect(status().isInternalServerError());
    }

    @Test

    void shouldNotHaveNPlusOneProblem() throws Exception {
        String token = loginAndGetToken("admin@gmail.com");

        SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
        Statistics statistics = sessionFactory.getStatistics();

        statistics.clear();

        mockMvc.perform(get("/api/v1/orders").header("Authorization","Bearer "
                        + token))
                .andExpect(status().isOk());

        long totalQueries = statistics.getPrepareStatementCount();

        assertEquals(2, totalQueries);
    }

}

package org.example.empresa.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.example.empresa.dto.product.v1.ProductRequestDto;
import org.example.empresa.dto.security.LoginRequestDto;
import org.example.empresa.entity.Category;
import org.example.empresa.entity.Product;
import org.example.empresa.entity.Role;
import org.example.empresa.entity.User;
import org.example.empresa.repository.CategoryRepository;
import org.example.empresa.repository.ProductRepository;
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

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ProductControllerIntegrationTest {

    private static final String ADMIN_EMAIL = "admin.products@test.com";
    private static final String PASSWORD = "12345";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private ProductRepository repository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Product existingProduct;
    private Category existingCategory;

    @BeforeEach
    void setUp() {
        existingCategory = categoryRepository.save(
                new Category(null, "Categoria Test")
        );

        existingProduct = repository.save(
                new Product(
                        null,
                        "Mouse Integration",
                        "Mouse inalambrico",
                        new BigDecimal("25.99"),
                        50,
                        null,
                        existingCategory
                )
        );

        User user = new User(
                null,
                ADMIN_EMAIL,
                passwordEncoder.encode(PASSWORD),
                Role.ADMIN,
                true
        );

        userRepository.save(user);
    }

    private String loginAndGetToken(String email) throws Exception {
        LoginRequestDto requestDto = new LoginRequestDto(email, PASSWORD);

        String response = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return mapper.readTree(response)
                .get("token")
                .asText();
    }

    @Test
    void shouldCreateProductSuccessfully() throws Exception {
        String token = loginAndGetToken(ADMIN_EMAIL);

        ProductRequestDto request = new ProductRequestDto(
                "Teclado Integration",
                "Teclado mecanico",
                new BigDecimal("45.99"),
                30,
                existingCategory.getId()
        );

        mockMvc.perform(post("/api/v1/products")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Teclado Integration"))
                .andExpect(jsonPath("$.price").value(45.99));
    }

    @Test
    void shouldReturn400WhenNameIsBlank() throws Exception {
        String token = loginAndGetToken(ADMIN_EMAIL);

        ProductRequestDto request = new ProductRequestDto(
                "",
                "Mouse inalambrico",
                new BigDecimal("25.99"),
                50,
                existingCategory.getId()
        );

        mockMvc.perform(post("/api/v1/products")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors").exists());
    }

    @Test
    void shouldReturn400WhenPriceIsNegative() throws Exception {
        String token = loginAndGetToken(ADMIN_EMAIL);

        ProductRequestDto request = new ProductRequestDto(
                "Mouse",
                "Mouse inalambrico",
                new BigDecimal("-25.99"),
                50,
                existingCategory.getId()
        );

        mockMvc.perform(post("/api/v1/products")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors").exists());
    }

    @Test
    void shouldFindProductById() throws Exception {
        String token = loginAndGetToken(ADMIN_EMAIL);

        mockMvc.perform(get("/api/v1/products/" + existingProduct.getId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingProduct.getId()))
                .andExpect(jsonPath("$.name").value(existingProduct.getName()))
                .andExpect(jsonPath("$.price").value(existingProduct.getPrice()));
    }

    @Test
    void shouldReturn404WhenProductNotFound() throws Exception {
        String token = loginAndGetToken(ADMIN_EMAIL);

        mockMvc.perform(get("/api/v1/products/9999")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Producto con id 9999 no encontrado"));
    }

    @Test
    void shouldUpdateProductSuccessfully() throws Exception {
        String token = loginAndGetToken(ADMIN_EMAIL);

        ProductRequestDto request = new ProductRequestDto(
                "Televisor",
                "Tele inalambrica",
                new BigDecimal("75.99"),
                50,
                existingCategory.getId()
        );

        mockMvc.perform(put("/api/v1/products/" + existingProduct.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(request.getName()))
                .andExpect(jsonPath("$.description").value(request.getDescription()))
                .andExpect(jsonPath("$.price").value(request.getPrice()));
    }

    @Test
    void shouldDeleteProductSuccessfully() throws Exception {
        String token = loginAndGetToken(ADMIN_EMAIL);

        mockMvc.perform(delete("/api/v1/products/" + existingProduct.getId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/products/" + existingProduct.getId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnPaginatedProduct() throws Exception {
        String token = loginAndGetToken(ADMIN_EMAIL);

        mockMvc.perform(get("/api/v1/products?size=20&page=0")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size").value(20))
                .andExpect(jsonPath("$.totalElements").exists());
    }

    @Test
    void shouldFilterProductsByNameAndPriceRange() throws Exception {
        String token = loginAndGetToken(ADMIN_EMAIL);

        mockMvc.perform(get("/api/v1/products/search?name=" + existingProduct.getName()
                        + "&minPrice=25.98&maxPrice=26.00")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].price").value(existingProduct.getPrice()))
                .andExpect(jsonPath("$.content[0].name").value(existingProduct.getName()));
    }
}
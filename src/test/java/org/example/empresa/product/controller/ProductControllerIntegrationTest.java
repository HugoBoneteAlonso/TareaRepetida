package org.example.empresa.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.example.empresa.dto.product.ProductRequestDto;
import org.example.empresa.entity.Category;
import org.example.empresa.entity.Product;
import org.example.empresa.repository.CategoryRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ProductControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private ProductRepository repository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Product existingProduct;
    private Category existingCategory;

    @BeforeEach
    void setUp() {
        existingProduct = repository.save(
                new Product(null, "Mouse", "Mouse inalambrico",
                        new BigDecimal("25.99"), 50, null, null));

        existingCategory = categoryRepository.save(
                new Category(null, "Categoria")
        );
    }

    @Test
    void shouldCreateProductSuccessfully() throws Exception{
        //Given
        ProductRequestDto request = new ProductRequestDto("Mouse",
                "Mouse inalambrico",new BigDecimal("25.00"),
                50, existingCategory.getId());

        //When + Then
        mockMvc.perform(post("/api/v1/products").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Mouse"))
                .andExpect(jsonPath("$.price").value(25.99));
    }

    @Test
    void shouldReturn400WhenNameIsBlank() throws Exception{
        //Given
        ProductRequestDto request = new ProductRequestDto("",
                "Mouse inalambrico",new BigDecimal("25.99"),
                50, existingCategory.getId());

        //When + Then
        mockMvc.perform(post("/api/v1/products").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors").exists());
    }

    @Test
    void shouldReturn400WhenPriceIsNegative() throws Exception{
        //Given
        ProductRequestDto request = new ProductRequestDto("Mouse",
                "Mouse inalambrico",new BigDecimal("-25.99"),
                50, existingCategory.getId());

        //When + Then
        mockMvc.perform(post("/api/v1/products").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors").exists());
    }

    @Test
    void shouldFindProductById() throws Exception {
        mockMvc.perform(get("/api/v1/products/" + existingProduct.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingProduct.getId()))
                .andExpect(jsonPath("$.name").value(existingProduct.getName()))
                .andExpect(jsonPath("$.price").value(existingProduct.getPrice()));
    }

    @Test
    void shouldReturn404WhenProductNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/products/9999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Producto con id 9999 no encontrado"));
    }

    @Test
    void shouldUpdateProductSuccessfully() throws Exception {
        ProductRequestDto request = new ProductRequestDto("Televisor",
                "Tele inalambrica",new BigDecimal("75.99"),
                50, existingCategory.getId());
        mockMvc.perform(put("/api/v1/products/" + existingProduct.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(request.getName()))
                .andExpect(jsonPath("$.description").value(request.getDescription()))
                .andExpect(jsonPath("$.price").value(request.getPrice()));
    }

    @Test
    void shouldDeleteProductSuccessfully() throws Exception {
        mockMvc.perform(delete("/api/v1/products/" + existingProduct.getId()))
                .andExpect(status().isNoContent());
        mockMvc.perform(get("/api/v1/products/" + existingProduct.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnPaginatedProduct() throws Exception {
        mockMvc.perform(get("/api/v1/products?size=20&page=0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size").value(20))
                .andExpect(jsonPath("$.totalElements").exists());
    }

    @Test
    void shouldFilterProductsByNameAndPriceRange() throws Exception {
        mockMvc.perform(get("/api/v1/products/search?name=" + existingProduct.getName()
                + "&minPrice=25.98&maxPrice=26.00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].price").value(existingProduct.getPrice()))
                .andExpect(jsonPath("$.content[0].name").value(existingProduct.getName()));
    }
}

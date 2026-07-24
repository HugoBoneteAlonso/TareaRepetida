package org.example.empresa.product.cache;

import jakarta.transaction.Transactional;
import org.example.empresa.repository.ProductRepository;
import org.example.empresa.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ProductCacheIntegrationTest {
    @Autowired
    private ProductService service;

    @SpyBean
    private ProductRepository repository;

    @Test
    void shouldUseCacheAfterFirstCall() {
        service.getById(1L);
        service.getById(1L);

        verify(repository, times(1)).findById(1L);
    }
}

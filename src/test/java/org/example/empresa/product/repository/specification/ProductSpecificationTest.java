package org.example.empresa.product.repository.specification;

import org.example.empresa.entity.Product;
import org.example.empresa.repository.ProductRepository;
import org.example.empresa.repository.specification.ProductSpecification;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProductSpecificationTest {
    @Autowired
    private ProductRepository repository;

    @Test
    void shouldFilterByNameAndPrice() {
        Product p1 = new Product();
        p1.setName("Telefono msimag");
        p1.setPrice(BigDecimal.valueOf(500));
        p1.setStock(10);

        Product p2 = new Product();
        p2.setName("Monitor lg");
        p2.setPrice(BigDecimal.valueOf(200));
        p2.setStock(5);

        repository.saveAll(List.of(p1, p2));

        Specification<Product> spec = Specification.where(ProductSpecification.hasNameLike("msimag")
                        .and(ProductSpecification.hasPriceBetween(BigDecimal.valueOf(300), null)));

        List<Product> result = repository.findAll(spec);

        assertThat(result)
                .hasSize(1);

        assertThat(result.get(0).getName())
                .isEqualTo("Telefono msimag");
    }
}

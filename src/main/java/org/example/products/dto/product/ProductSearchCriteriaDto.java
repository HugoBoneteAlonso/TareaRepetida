package org.example.products.dto.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@Setter
public class ProductSearchCriteriaDto {
    private String name;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Integer minStock;
    private Long category;
}

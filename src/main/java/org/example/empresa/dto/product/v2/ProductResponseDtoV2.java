package org.example.empresa.dto.product.v2;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.empresa.dto.product.v1.CategoryResponseDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ProductResponseDtoV2 {
    private BigDecimal id;
    private String name;
    private String description;
    private Integer stock;
    private LocalDateTime createdAt;
    private CategoryResponseDto category;
    private MoneyDto price;
}

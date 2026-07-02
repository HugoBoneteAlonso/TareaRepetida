package org.example.products.dto.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CategoryResponseDto {
    private Long id;
    private String name;
}

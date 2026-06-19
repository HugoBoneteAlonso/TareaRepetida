package org.example.products.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDto {

    @Size(max = 50)
    private String name;

    @Size(max = 100)
    private String description;

    @Positive
    private BigDecimal price;

    @Min(0)
    private Integer stock;
}

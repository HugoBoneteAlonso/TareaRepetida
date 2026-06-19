package org.example.products.dto;

import jakarta.validation.constraints.*;
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
    @NotBlank
    private String name;

    @Size(max = 100)
    private String description;

    @Positive
    @NotNull
    private BigDecimal price;

    @Min(0)
    @NotNull
    private Integer stock;
}

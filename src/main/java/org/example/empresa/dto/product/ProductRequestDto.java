package org.example.empresa.dto.product;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.empresa.constraint.ValidName;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDto {
    @Size(max = 50, message = "El nombre debe tener menos de 50 caracteres")
    @NotBlank(message = "El nombre no puede ser nulo")
    @ValidName
    private String name;

    @Size(max = 100, message = "La descripcion debe tener menos de 100 caracteres")
    private String description;

    @Positive(message = "El precio debe ser positivo")
    @NotNull(message = "El precion no puede ser nulo")
    private BigDecimal price;

    @PositiveOrZero(message = "El stock debe ser igual o mayor a 0")
    @NotNull(message = "El stock no puede ser nulo")
    private Integer stock;

    private Long category;
}

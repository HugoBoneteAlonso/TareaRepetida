package org.example.products.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderLineRequestDto {
    @NotNull
    private Long customerId;

    private List<CreateOrderLineRequestDto> lines;
}

package org.example.empresa.dto.product.v2;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class MoneyDto {
    private BigDecimal amount;
    private String currency;
}

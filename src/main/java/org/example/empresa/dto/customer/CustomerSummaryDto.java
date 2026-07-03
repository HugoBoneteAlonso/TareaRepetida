package org.example.empresa.dto.customer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerSummaryDto {
    private Long id;
    private String name;
    private String email;
    private Integer totalOrders;
    private BigDecimal totalSpent;
}

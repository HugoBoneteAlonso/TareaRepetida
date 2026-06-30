package org.example.products.dto.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.products.dto.customer.CustomerSummaryDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDto {
    private Long id;
    private LocalDateTime orderDate;
    private String status;
    private CustomerSummaryDto customer;
    private List<OrderLineResponseDto> lines;
    private BigDecimal totalAmount;
}

package org.example.products.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 50)
    @NotBlank
    private String name;

    @Size(max = 100)
    private String description;

    @Positive
    private BigDecimal price;

    @Min(0)
    private Integer stock;

    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime createdAt;
}

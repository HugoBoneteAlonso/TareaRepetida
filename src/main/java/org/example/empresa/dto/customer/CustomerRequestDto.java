package org.example.empresa.dto.customer;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CustomerRequestDto {
    @Size(max = 100)
    @NotBlank
    private String name;

    @Column(unique = true)
    @NotBlank
    private String email;

    @Size(max = 20)
    private String phone;
}

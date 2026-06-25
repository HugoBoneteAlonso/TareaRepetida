package org.example.products.dto.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class FieldErrorDto {
    private String field;

    private Object rejectedValue;

    private String message;
}

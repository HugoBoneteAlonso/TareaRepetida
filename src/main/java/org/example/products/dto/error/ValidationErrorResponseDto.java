package org.example.products.dto.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ValidationErrorResponseDto {

    private LocalDateTime timeStamp;

    private int status;

    private String error;

    private String message;

    private String path;

    private List<FieldErrorDto> fieldErrors;
}

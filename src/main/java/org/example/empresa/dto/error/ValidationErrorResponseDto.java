package org.example.empresa.dto.error;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ValidationErrorResponseDto extends ErrorResponseDto{
    private List<FieldErrorDto> fieldErrors;

    public ValidationErrorResponseDto(LocalDateTime timeStamp, int status, String error,
                                      String message, String path, UUID traceId, List<FieldErrorDto> fieldErrors) {
        super(timeStamp, status, error, message, path, traceId);
        this.fieldErrors = fieldErrors;
    }
}

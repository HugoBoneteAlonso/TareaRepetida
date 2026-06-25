package org.example.products.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.example.products.dto.error.ErrorResponseDto;
import org.example.products.dto.error.FieldErrorDto;
import org.example.products.dto.error.ValidationErrorResponseDto;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponseDto handleMethodArgumentNotValidException(MethodArgumentNotValidException ex
    , HttpServletRequest request) {
        List<FieldErrorDto> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fe -> new FieldErrorDto(
                        fe.getField(),
                        fe.getRejectedValue(),
                        fe.getDefaultMessage()))
                .toList();

        return new ValidationErrorResponseDto(LocalDateTime.now(), 400, "Bad Request",
                "La peticion contiene campos invalidos", request.getRequestURI(),
                fieldErrors);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDto handleProductNotFoundException(ProductNotFoundException ex, HttpServletRequest request) {
        return new ErrorResponseDto(LocalDateTime.now(), 404,"Product Not Found",
                ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponseDto handleGenericException(Exception ex, HttpServletRequest request) {

        return new ErrorResponseDto(LocalDateTime.now(), 404,"Internal Server Error",
                "Ha ocurrido una error inesperado", request.getRequestURI());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleHttpMessageNotReadableException(HttpMessageNotReadableException ex
            , HttpServletRequest request) {

        return new ErrorResponseDto(LocalDateTime.now(), 400, "Bad Request",
                "Los campos del body estan mal formados", request.getRequestURI());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException  ex
            , HttpServletRequest request) {
        return new ErrorResponseDto(LocalDateTime.now(), 400, "Bad Request",
                "Parametro del path con tipo incorrecto", request.getRequestURI());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleDataIntegrityViolationException(DataIntegrityViolationException ex
            , HttpServletRequest request) {
        return new ErrorResponseDto(LocalDateTime.now(), 409, "Bad Request",
                "Ya existe un producto con este nombre", request.getRequestURI());
    }
}

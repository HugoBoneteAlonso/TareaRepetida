package org.example.empresa.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.example.empresa.dto.error.ErrorResponseDto;
import org.example.empresa.dto.error.FieldErrorDto;
import org.example.empresa.dto.error.ValidationErrorResponseDto;
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
import java.util.UUID;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final String BAD_REQUEST = "Bad Request";

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

        return new ValidationErrorResponseDto(LocalDateTime.now(), 400, BAD_REQUEST,
                "La peticion contiene campos invalidos", request.getRequestURI(), UUID.randomUUID(),
                fieldErrors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleConstraintViolationException(
            ConstraintViolationException ex,
            HttpServletRequest request) {

        return new ErrorResponseDto(LocalDateTime.now(), 400, BAD_REQUEST,
                ex.getMessage(), request.getRequestURI(), UUID.randomUUID());
    }

    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDto handleProductNotFoundException(ProductNotFoundException ex, HttpServletRequest request) {
        return new ErrorResponseDto(LocalDateTime.now(), 404,"Product Not Found",
                ex.getMessage(), request.getRequestURI(),UUID.randomUUID());
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDto handleCustomerNotFoundException(CustomerNotFoundException ex, HttpServletRequest request) {
        return new ErrorResponseDto(LocalDateTime.now(), 404,"Customer Not Found",
                ex.getMessage(), request.getRequestURI(),UUID.randomUUID());
    }

    @ExceptionHandler(OrderNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDto handleOrderNotFoundException(OrderNotFoundException ex, HttpServletRequest request) {
        return new ErrorResponseDto(LocalDateTime.now(), 404,"Order Not Found",
                ex.getMessage(), request.getRequestURI(),UUID.randomUUID());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponseDto handleGenericException(Exception ex, HttpServletRequest request) {

        return new ErrorResponseDto(LocalDateTime.now(), 500,"Internal Server Error",
                "Ha ocurrido una error inesperado", request.getRequestURI(), UUID.randomUUID());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleHttpMessageNotReadableException(HttpMessageNotReadableException ex
            , HttpServletRequest request) {

        return new ErrorResponseDto(LocalDateTime.now(), 400, BAD_REQUEST,
                "Los campos del body estan mal formados", request.getRequestURI(), UUID.randomUUID());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException  ex
            , HttpServletRequest request) {
        return new ErrorResponseDto(LocalDateTime.now(), 400, BAD_REQUEST,
                "Parametro del path con tipo incorrecto", request.getRequestURI(), UUID.randomUUID());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleDataIntegrityViolationException(DataIntegrityViolationException ex
            , HttpServletRequest request) {
        return new ErrorResponseDto(LocalDateTime.now(), 409, BAD_REQUEST,
                "Ya existe un producto con este nombre", request.getRequestURI(), UUID.randomUUID());
    }
}

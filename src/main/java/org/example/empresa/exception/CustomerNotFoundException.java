package org.example.empresa.exception;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(Long id) {
        super("Cliente con id " + id + " no encontrado");
    }
}

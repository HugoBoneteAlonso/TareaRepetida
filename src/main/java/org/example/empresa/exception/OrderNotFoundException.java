package org.example.empresa.exception;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(Long id) {
        super("Pedido con id " + id + " no encontrado");
    }
}

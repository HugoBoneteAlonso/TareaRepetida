package org.example.empresa.exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long id) {
        super("Producto con id " + id + " no encontrado");
    }
}

package org.example.empresa.exception;

public class ProductInUseException extends RuntimeException {
    public ProductInUseException(Long id) {
        super("No se puede eliminar el producto " + id +
                " porque está asociado a pedidos");
    }
}

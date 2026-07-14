package org.example.empresa.exception;

public class TooManyRequestsException extends RuntimeException {
    public TooManyRequestsException() {
        super("Demasiados intentos vuelve a intentarlo dentro de 1 minuto");
    }
}

package com.example.management.domain.exception;

/**
 * Excepción de dominio para violaciones de invariantes en la gestión de pedidos.
 * La capa de dominio no depende de frameworks; usa solo JDK.
 */
public class OrderDomainException extends RuntimeException {

    public OrderDomainException(String message) {
        super(message);
    }

    public OrderDomainException(String message, Throwable cause) {
        super(message, cause);
    }
}

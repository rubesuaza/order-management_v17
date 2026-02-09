package com.example.management.domain.model;

/**
 * Estados posibles de un pedido en el dominio.
 */
public enum OrderStatus {
    DRAFT,
    CONFIRMED,
    SHIPPED,
    CANCELLED
}

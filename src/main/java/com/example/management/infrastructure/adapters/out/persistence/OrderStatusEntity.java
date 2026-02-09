package com.example.management.infrastructure.adapters.out.persistence;

/**
 * Representación del estado del pedido en persistencia.
 * Mapea desde/hacia el enum de dominio OrderStatus.
 */
public enum OrderStatusEntity {
    DRAFT,
    CONFIRMED,
    SHIPPED,
    CANCELLED
}

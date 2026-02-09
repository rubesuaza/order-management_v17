package com.example.management.infrastructure.adapters.in.web;

import java.math.BigDecimal;

/**
 * DTO de entrada para una línea de pedido en las peticiones REST.
 */
public record OrderLineRequest(String productId, int quantity, BigDecimal unitPrice) {
}

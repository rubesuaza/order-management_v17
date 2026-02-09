package com.example.management.infrastructure.adapters.in.web;

import java.util.List;

/**
 * DTO de entrada para crear o representar un pedido en las peticiones REST.
 */
public record OrderRequest(String id, String customerId, String status, List<OrderLineRequest> lines) {
}

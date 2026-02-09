package com.example.management.application.ports.in;

import com.example.management.domain.model.Order;

import java.util.Optional;

/**
 * Puerto de entrada: caso de uso para obtener un pedido por id.
 */
public interface GetOrderUseCase {

    Optional<Order> getById(String id);
}

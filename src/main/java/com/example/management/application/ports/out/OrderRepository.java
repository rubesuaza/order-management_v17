package com.example.management.application.ports.out;

import com.example.management.domain.model.Order;

import java.util.Optional;

/**
 * Puerto de salida para persistencia de pedidos.
 * La infraestructura proporciona implementaciones (adaptadores).
 */
public interface OrderRepository {

    Order save(Order order);

    Optional<Order> findById(String id);
}

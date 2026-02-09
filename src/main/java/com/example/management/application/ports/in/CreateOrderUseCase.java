package com.example.management.application.ports.in;

import com.example.management.domain.model.Order;

/**
 * Puerto de entrada: caso de uso para crear un pedido.
 */
public interface CreateOrderUseCase {

    Order create(Order order);
}

package com.example.management.application.services;

import com.example.management.application.ports.in.CreateOrderUseCase;
import com.example.management.application.ports.in.GetOrderUseCase;
import com.example.management.application.ports.out.OrderRepository;
import com.example.management.domain.model.Order;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Servicio de aplicación que orquesta los casos de uso de pedidos.
 * Implementa los puertos de entrada y delega la persistencia en el puerto de salida.
 */
@Service
public class OrderApplicationService implements CreateOrderUseCase, GetOrderUseCase {

    private final OrderRepository orderRepository;

    public OrderApplicationService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order create(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public Optional<Order> getById(String id) {
        return orderRepository.findById(id);
    }
}

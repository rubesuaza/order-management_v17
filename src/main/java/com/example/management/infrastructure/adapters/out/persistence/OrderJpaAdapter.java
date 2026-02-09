package com.example.management.infrastructure.adapters.out.persistence;

import com.example.management.application.ports.out.OrderRepository;
import com.example.management.domain.model.Order;
import com.example.management.domain.model.OrderLine;
import com.example.management.domain.model.OrderStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Adaptador de salida: persiste y recupera pedidos usando JPA.
 * Traduce entre entidades de dominio y entidades JPA.
 */
@Component
public class OrderJpaAdapter implements OrderRepository {

    private final OrderSpringRepository springRepository;

    public OrderJpaAdapter(OrderSpringRepository springRepository) {
        this.springRepository = springRepository;
    }

    @Override
    public Order save(Order order) {
        OrderEntity entity = toEntity(order);
        OrderEntity saved = springRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Order> findById(String id) {
        return springRepository.findByIdWithLines(id).map(this::toDomain);
    }

    private OrderEntity toEntity(Order order) {
        List<OrderLineEntity> lineEntities = order.getLines().stream()
                .map(l -> new OrderLineEntity(l.getProductId(), l.getQuantity(), l.getUnitPrice()))
                .toList();
        return new OrderEntity(
                order.getId(),
                order.getCustomerId(),
                OrderStatusEntity.valueOf(order.getStatus().name()),
                lineEntities
        );
    }

    private Order toDomain(OrderEntity entity) {
        List<OrderLine> lines = entity.getLines().stream()
                .map(l -> new OrderLine(l.getProductId(), l.getQuantity(), l.getUnitPrice()))
                .toList();
        return new Order(
                entity.getId(),
                entity.getCustomerId(),
                OrderStatus.valueOf(entity.getStatus().name()),
                lines
        );
    }
}

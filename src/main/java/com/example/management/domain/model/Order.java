package com.example.management.domain.model;

import com.example.management.domain.exception.OrderDomainException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Entidad de dominio que representa un pedido.
 * Invariantes: al menos una línea, customerId no nulo ni vacío, status no nulo.
 * El total del pedido es siempre la suma de los totales de las líneas (calculado).
 */
public final class Order {

    private final String id;
    private final String customerId;
    private final OrderStatus status;
    private final List<OrderLine> lines;

    public Order(String id, String customerId, OrderStatus status, List<OrderLine> lines) {
        if (customerId == null || customerId.isBlank()) {
            throw new OrderDomainException("El customerId no puede ser nulo ni vacío");
        }
        if (status == null) {
            throw new OrderDomainException("El estado del pedido no puede ser nulo");
        }
        if (lines == null || lines.isEmpty()) {
            throw new OrderDomainException("El pedido debe tener al menos una línea");
        }
        this.id = id;
        this.customerId = customerId.trim();
        this.status = status;
        this.lines = new ArrayList<>(lines);
    }

    public String getId() {
        return id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    /**
     * Devuelve una copia de las líneas para preservar la inmutabilidad.
     */
    public List<OrderLine> getLines() {
        return Collections.unmodifiableList(new ArrayList<>(lines));
    }

    /**
     * Total del pedido: suma de los totales de todas las líneas.
     */
    public BigDecimal getTotal() {
        return lines.stream()
                .map(OrderLine::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

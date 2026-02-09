package com.example.management.infrastructure.adapters.out.persistence;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad JPA para persistencia de pedidos.
 * Solo existe en la capa de infraestructura; el dominio no conoce JPA.
 */
@Entity
@Table(name = "orders")
public class OrderEntity {

    @Id
    private String id;

    @Column(name = "customer_id", nullable = false)
    private String customerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatusEntity status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLineEntity> lines = new ArrayList<>();

    protected OrderEntity() {}

    public OrderEntity(String id, String customerId, OrderStatusEntity status, List<OrderLineEntity> lines) {
        this.id = id;
        this.customerId = customerId;
        this.status = status;
        if (lines != null) {
            this.lines.addAll(lines);
            this.lines.forEach(l -> l.setOrder(this));
        }
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public OrderStatusEntity getStatus() { return status; }
    public void setStatus(OrderStatusEntity status) { this.status = status; }
    public List<OrderLineEntity> getLines() { return lines; }
    public void setLines(List<OrderLineEntity> lines) { this.lines = lines; }
}

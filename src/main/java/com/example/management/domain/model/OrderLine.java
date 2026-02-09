package com.example.management.domain.model;

import com.example.management.domain.exception.OrderDomainException;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Value object que representa una línea de pedido.
 * Invariantes: quantity > 0, unitPrice >= 0, productId no nulo ni vacío.
 * lineTotal = quantity * unitPrice (calculado).
 */
public final class OrderLine {

    private final String productId;
    private final int quantity;
    private final BigDecimal unitPrice;

    public OrderLine(String productId, int quantity, BigDecimal unitPrice) {
        if (productId == null || productId.isBlank()) {
            throw new OrderDomainException("El productId no puede ser nulo ni vacío");
        }
        if (quantity <= 0) {
            throw new OrderDomainException("La cantidad debe ser mayor que cero");
        }
        if (unitPrice == null) {
            throw new OrderDomainException("El precio unitario no puede ser nulo");
        }
        if (unitPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new OrderDomainException("El precio unitario no puede ser negativo");
        }
        this.productId = productId.trim();
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public String getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    /**
     * Total de la línea: cantidad * precio unitario.
     */
    public BigDecimal getLineTotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderLine orderLine = (OrderLine) o;
        return quantity == orderLine.quantity
                && Objects.equals(productId, orderLine.productId)
                && Objects.equals(unitPrice, orderLine.unitPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, quantity, unitPrice);
    }
}

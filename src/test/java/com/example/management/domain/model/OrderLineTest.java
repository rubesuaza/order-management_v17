package com.example.management.domain.model;

import com.example.management.domain.exception.OrderDomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("OrderLine")
class OrderLineTest {

    @Nested
    @DisplayName("construcción válida")
    class ValidConstruction {
        @Test
        void crea_linea_con_cantidad_y_precio_unitario_positivos() {
            OrderLine line = new OrderLine("PROD-1", 2, new BigDecimal("10.50"));

            assertEquals("PROD-1", line.getProductId());
            assertEquals(2, line.getQuantity());
            assertEquals(new BigDecimal("10.50"), line.getUnitPrice());
            assertEquals(new BigDecimal("21.00"), line.getLineTotal());
        }

        @Test
        void total_de_linea_es_cantidad_por_precio_unitario() {
            OrderLine line = new OrderLine("P2", 3, new BigDecimal("5.25"));
            assertEquals(new BigDecimal("15.75"), line.getLineTotal());
        }

        @Test
        void acepta_precio_unitario_cero() {
            OrderLine line = new OrderLine("P3", 1, BigDecimal.ZERO);
            assertEquals(BigDecimal.ZERO, line.getLineTotal());
        }
    }

    @Nested
    @DisplayName("invariantes")
    class Invariants {
        @Test
        void rechaza_cantidad_cero() {
            assertThrows(OrderDomainException.class, () ->
                    new OrderLine("P1", 0, new BigDecimal("10.00")));
        }

        @Test
        void rechaza_cantidad_negativa() {
            assertThrows(OrderDomainException.class, () ->
                    new OrderLine("P1", -1, new BigDecimal("10.00")));
        }

        @Test
        void rechaza_precio_unitario_negativo() {
            assertThrows(OrderDomainException.class, () ->
                    new OrderLine("P1", 1, new BigDecimal("-5.00")));
        }

        @Test
        void rechaza_productId_nulo() {
            assertThrows(OrderDomainException.class, () ->
                    new OrderLine(null, 1, new BigDecimal("10.00")));
        }

        @Test
        void rechaza_productId_vacio() {
            assertThrows(OrderDomainException.class, () ->
                    new OrderLine("", 1, new BigDecimal("10.00")));
        }

        @Test
        void rechaza_unitPrice_nulo() {
            assertThrows(OrderDomainException.class, () ->
                    new OrderLine("P1", 1, null));
        }
    }
}

package com.example.management.domain.model;

import com.example.management.domain.exception.OrderDomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Order")
class OrderTest {

    private static OrderLine line(String productId, int qty, String price) {
        return new OrderLine(productId, qty, new BigDecimal(price));
    }

    @Nested
    @DisplayName("construcción válida")
    class ValidConstruction {
        @Test
        void crea_pedido_con_lineas_y_total_calculado() {
            Order order = new Order("ORD-1", "CUST-1", OrderStatus.DRAFT,
                    List.of(line("P1", 2, "10.00"), line("P2", 1, "5.50")));

            assertEquals("ORD-1", order.getId());
            assertEquals("CUST-1", order.getCustomerId());
            assertEquals(OrderStatus.DRAFT, order.getStatus());
            assertEquals(2, order.getLines().size());
            assertEquals(new BigDecimal("25.50"), order.getTotal());
        }

        @Test
        void total_es_suma_de_totales_de_lineas() {
            Order order = new Order("O2", "C2", OrderStatus.CONFIRMED,
                    List.of(line("A", 3, "2.00"), line("B", 2, "3.00")));
            assertEquals(new BigDecimal("12.00"), order.getTotal());
        }
    }

    @Nested
    @DisplayName("invariantes")
    class Invariants {
        @Test
        void rechaza_lista_de_lineas_nula() {
            assertThrows(OrderDomainException.class, () ->
                    new Order("O1", "C1", OrderStatus.DRAFT, null));
        }

        @Test
        void rechaza_lista_de_lineas_vacia() {
            assertThrows(OrderDomainException.class, () ->
                    new Order("O1", "C1", OrderStatus.DRAFT, List.of()));
        }

        @Test
        void rechaza_customerId_nulo() {
            assertThrows(OrderDomainException.class, () ->
                    new Order("O1", null, OrderStatus.DRAFT, List.of(line("P1", 1, "1.00"))));
        }

        @Test
        void rechaza_customerId_vacio() {
            assertThrows(OrderDomainException.class, () ->
                    new Order("O1", "  ", OrderStatus.DRAFT, List.of(line("P1", 1, "1.00"))));
        }

        @Test
        void rechaza_status_nulo() {
            assertThrows(OrderDomainException.class, () ->
                    new Order("O1", "C1", null, List.of(line("P1", 1, "1.00"))));
        }
    }

    @Nested
    @DisplayName("inmutabilidad de líneas")
    class LinesImmutability {
        @Test
        void getLines_devuelve_copia_para_evitar_modificacion_externa() {
            List<OrderLine> lines = List.of(line("P1", 1, "10.00"));
            Order order = new Order("O1", "C1", OrderStatus.DRAFT, lines);
            List<OrderLine> returned = order.getLines();
            assertNotSame(lines, returned);
            assertEquals(lines, returned);
        }
    }
}

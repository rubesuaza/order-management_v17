package com.example.management.application.services;

import com.example.management.application.ports.out.OrderRepository;
import com.example.management.domain.model.Order;
import com.example.management.domain.model.OrderLine;
import com.example.management.domain.model.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("OrderApplicationService")
class OrderApplicationServiceTest {

    @Mock
    OrderRepository orderRepository;

    OrderApplicationService service;

    @BeforeEach
    void setUp() {
        service = new OrderApplicationService(orderRepository);
    }

    private static Order order(String id, String customerId, OrderStatus status, List<OrderLine> lines) {
        return new Order(id, customerId, status, lines);
    }

    private static OrderLine line(String productId, int qty, BigDecimal price) {
        return new OrderLine(productId, qty, price);
    }

    @Nested
    @DisplayName("create")
    class Create {
        @Test
        void delega_en_repositorio_y_devuelve_pedido_guardado() {
            Order toSave = order("O1", "C1", OrderStatus.DRAFT,
                    List.of(line("P1", 2, new BigDecimal("10.00"))));
            Order saved = order("O1", "C1", OrderStatus.DRAFT,
                    List.of(line("P1", 2, new BigDecimal("10.00"))));
            when(orderRepository.save(any(Order.class))).thenReturn(saved);

            Order result = service.create(toSave);

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo("O1");
            assertThat(result.getCustomerId()).isEqualTo("C1");
            assertThat(result.getTotal()).isEqualByComparingTo(new BigDecimal("20.00"));
            verify(orderRepository).save(toSave);
        }
    }

    @Nested
    @DisplayName("getById")
    class GetById {
        @Test
        void devuelve_Optional_con_pedido_cuando_existe() {
            Order order = order("O2", "C2", OrderStatus.CONFIRMED,
                    List.of(line("P2", 1, new BigDecimal("15.00"))));
            when(orderRepository.findById("O2")).thenReturn(Optional.of(order));

            Optional<Order> result = service.getById("O2");

            assertThat(result).isPresent();
            assertThat(result.get().getId()).isEqualTo("O2");
            assertThat(result.get().getCustomerId()).isEqualTo("C2");
            verify(orderRepository).findById("O2");
        }

        @Test
        void devuelve_Optional_vacio_cuando_no_existe() {
            when(orderRepository.findById(eq("missing"))).thenReturn(Optional.empty());

            Optional<Order> result = service.getById("missing");

            assertThat(result).isEmpty();
            verify(orderRepository).findById("missing");
        }
    }
}

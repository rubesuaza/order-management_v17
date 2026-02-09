package com.example.management.infrastructure.adapters.out.persistence;

import com.example.management.application.ports.out.OrderRepository;
import com.example.management.domain.model.Order;
import com.example.management.domain.model.OrderLine;
import com.example.management.domain.model.OrderStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests de contrato/integración del adaptador de persistencia de pedidos.
 * Verifica el comportamiento esperado contra base en memoria (H2).
 */
@DataJpaTest
@ActiveProfiles("test")
@Import(OrderJpaAdapter.class)
class OrderJpaAdapterTest {

    @Autowired
    OrderRepository orderRepository;

    @Test
    void save_and_findById_returns_saved_order() {
        Order order = new Order(
                "order-1",
                "customer-1",
                OrderStatus.DRAFT,
                List.of(
                        new OrderLine("prod-A", 2, new BigDecimal("10.50")),
                        new OrderLine("prod-B", 1, new BigDecimal("25.00"))
                )
        );

        Order saved = orderRepository.save(order);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isEqualTo("order-1");
        assertThat(saved.getCustomerId()).isEqualTo("customer-1");
        assertThat(saved.getStatus()).isEqualTo(OrderStatus.DRAFT);
        assertThat(saved.getLines()).hasSize(2);
        assertThat(saved.getTotal()).isEqualByComparingTo(new BigDecimal("46.00"));

        Order found = orderRepository.findById("order-1").orElseThrow();
        assertThat(found.getId()).isEqualTo(saved.getId());
        assertThat(found.getCustomerId()).isEqualTo(saved.getCustomerId());
        assertThat(found.getStatus()).isEqualTo(saved.getStatus());
        assertThat(found.getLines()).hasSize(2);
        assertThat(found.getTotal()).isEqualByComparingTo(saved.getTotal());
    }

    @Test
    void findById_returns_empty_when_not_found() {
        assertThat(orderRepository.findById("non-existent")).isEmpty();
    }
}

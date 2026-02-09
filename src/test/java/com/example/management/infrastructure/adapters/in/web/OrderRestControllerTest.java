package com.example.management.infrastructure.adapters.in.web;

import com.example.management.application.ports.in.CreateOrderUseCase;
import com.example.management.application.ports.in.GetOrderUseCase;
import com.example.management.domain.model.Order;
import com.example.management.domain.model.OrderLine;
import com.example.management.domain.model.OrderStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests de contrato del adaptador de entrada REST para pedidos.
 */
@WebMvcTest(OrderRestController.class)
class OrderRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    CreateOrderUseCase createOrderUseCase;

    @MockBean
    GetOrderUseCase getOrderUseCase;

    @Test
    void createOrder_returns_201_and_order_body() throws Exception {
        OrderRequest request = new OrderRequest(
                "order-1",
                "customer-1",
                "DRAFT",
                List.of(
                        new OrderLineRequest("prod-A", 2, new BigDecimal("10.50"))
                )
        );
        Order saved = new Order(
                "order-1",
                "customer-1",
                OrderStatus.DRAFT,
                List.of(new OrderLine("prod-A", 2, new BigDecimal("10.50")))
        );
        when(createOrderUseCase.create(any(Order.class))).thenReturn(saved);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/orders/order-1"))
                .andExpect(jsonPath("$.id").value("order-1"))
                .andExpect(jsonPath("$.customerId").value("customer-1"))
                .andExpect(jsonPath("$.status").value("DRAFT"))
                .andExpect(jsonPath("$.lines.length()").value(1))
                .andExpect(jsonPath("$.total").value(21.0));
    }

    @Test
    void getOrder_returns_200_and_order_when_found() throws Exception {
        Order order = new Order(
                "order-1",
                "customer-1",
                OrderStatus.CONFIRMED,
                List.of(new OrderLine("prod-A", 1, new BigDecimal("15.00")))
        );
        when(getOrderUseCase.getById("order-1")).thenReturn(Optional.of(order));

        mockMvc.perform(get("/api/orders/order-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("order-1"))
                .andExpect(jsonPath("$.customerId").value("customer-1"))
                .andExpect(jsonPath("$.status").value("CONFIRMED"))
                .andExpect(jsonPath("$.total").value(15.0));
    }

    @Test
    void getOrder_returns_404_when_not_found() throws Exception {
        when(getOrderUseCase.getById("missing")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/orders/missing"))
                .andExpect(status().isNotFound());
    }
}

package com.example.management.infrastructure.adapters.in.web;

import com.example.management.application.ports.in.CreateOrderUseCase;
import com.example.management.application.ports.in.GetOrderUseCase;
import com.example.management.domain.model.Order;
import com.example.management.domain.model.OrderLine;
import com.example.management.domain.model.OrderStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Adaptador de entrada REST para la gestión de pedidos.
 */
@RestController
@RequestMapping("/api/orders")
public class OrderRestController {

    private final CreateOrderUseCase createOrderUseCase;
    private final GetOrderUseCase getOrderUseCase;

    public OrderRestController(CreateOrderUseCase createOrderUseCase, GetOrderUseCase getOrderUseCase) {
        this.createOrderUseCase = createOrderUseCase;
        this.getOrderUseCase = getOrderUseCase;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderResponse> create(@RequestBody OrderRequest request) {
        Order order = toDomain(request);
        Order saved = createOrderUseCase.create(order);
        OrderResponse body = toResponse(saved);
        return ResponseEntity
                .created(java.net.URI.create("/api/orders/" + saved.getId()))
                .body(body);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderResponse> getById(@PathVariable String id) {
        Optional<Order> order = getOrderUseCase.getById(id);
        return order
                .map(o -> ResponseEntity.ok(toResponse(o)))
                .orElse(ResponseEntity.notFound().build());
    }

    private Order toDomain(OrderRequest request) {
        List<OrderLine> lines = request.lines().stream()
                .map(l -> new OrderLine(l.productId(), l.quantity(), l.unitPrice()))
                .toList();
        return new Order(
                request.id(),
                request.customerId(),
                OrderStatus.valueOf(request.status()),
                lines
        );
    }

    private OrderResponse toResponse(Order order) {
        List<OrderLineResponse> lineResponses = order.getLines().stream()
                .map(l -> new OrderLineResponse(l.getProductId(), l.getQuantity(), l.getUnitPrice()))
                .toList();
        return new OrderResponse(
                order.getId(),
                order.getCustomerId(),
                order.getStatus().name(),
                lineResponses,
                order.getTotal()
        );
    }

    /** DTO de salida para una línea de pedido. */
    public record OrderLineResponse(String productId, int quantity, BigDecimal unitPrice) {
    }

    /** DTO de salida para un pedido en las respuestas REST. */
    public record OrderResponse(String id, String customerId, String status,
                                List<OrderLineResponse> lines, BigDecimal total) {
    }
}

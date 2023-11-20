package com.example.orderpipeline.controller;

import com.example.orderpipeline.consumer.OrderConsumer;
import com.example.orderpipeline.model.Order;
import com.example.orderpipeline.producer.OrderProducer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderProducer orderProducer;
    private final OrderConsumer orderConsumer;

    public OrderController(OrderProducer orderProducer, OrderConsumer orderConsumer) {
        this.orderProducer = orderProducer;
        this.orderConsumer = orderConsumer;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> createOrder(@RequestBody Order order) {
        order.setOrderId(UUID.randomUUID().toString().substring(0, 8));
        orderProducer.sendOrder(order);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(Map.of(
                        "orderId", order.getOrderId(),
                        "message", "Order submitted for processing"
                ));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrder(@PathVariable String orderId) {
        Order order = orderConsumer.getProcessedOrders().get(orderId);
        if (order == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Order not found or still processing"));
        }
        return ResponseEntity.ok(order);
    }

    @GetMapping
    public Collection<Order> getAllOrders() {
        return orderConsumer.getProcessedOrders().values();
    }
}

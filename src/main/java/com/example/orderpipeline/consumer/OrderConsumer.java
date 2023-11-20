package com.example.orderpipeline.consumer;

import com.example.orderpipeline.config.KafkaConfig;
import com.example.orderpipeline.model.Order;
import com.example.orderpipeline.model.OrderStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OrderConsumer {

    private static final Logger log = LoggerFactory.getLogger(OrderConsumer.class);

    private final KafkaTemplate<String, Order> kafkaTemplate;
    private final Map<String, Order> processedOrders = new ConcurrentHashMap<>();

    public OrderConsumer(KafkaTemplate<String, Order> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = KafkaConfig.ORDERS_TOPIC, groupId = "order-processor")
    public void processOrder(Order order) {
        log.info("Received order: {}", order);

        order.setStatus(OrderStatus.PROCESSING);
        log.info("Processing order {}...", order.getOrderId());

        try {
            // Simulate order validation and processing
            validateOrder(order);
            order.setStatus(OrderStatus.CONFIRMED);
            log.info("Order {} confirmed", order.getOrderId());
        } catch (Exception e) {
            order.setStatus(OrderStatus.FAILED);
            log.error("Order {} failed: {}", order.getOrderId(), e.getMessage());
        }

        processedOrders.put(order.getOrderId(), order);

        // Publish status update to downstream consumers
        kafkaTemplate.send(KafkaConfig.ORDER_STATUS_TOPIC, order.getOrderId(), order);
    }

    @KafkaListener(topics = KafkaConfig.ORDER_STATUS_TOPIC, groupId = "order-notifications")
    public void handleStatusUpdate(Order order) {
        log.info("Status update — Order {}: {}", order.getOrderId(), order.getStatus());
    }

    public Map<String, Order> getProcessedOrders() {
        return processedOrders;
    }

    private void validateOrder(Order order) {
        if (order.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        if (order.getPrice() == null || order.getPrice().signum() <= 0) {
            throw new IllegalArgumentException("Price must be greater than 0");
        }
    }
}

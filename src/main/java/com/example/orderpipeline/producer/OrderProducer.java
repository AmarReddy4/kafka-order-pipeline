package com.example.orderpipeline.producer;

import com.example.orderpipeline.config.KafkaConfig;
import com.example.orderpipeline.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class OrderProducer {

    private static final Logger log = LoggerFactory.getLogger(OrderProducer.class);

    private final KafkaTemplate<String, Order> kafkaTemplate;

    public OrderProducer(KafkaTemplate<String, Order> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendOrder(Order order) {
        CompletableFuture<SendResult<String, Order>> future =
                kafkaTemplate.send(KafkaConfig.ORDERS_TOPIC, order.getOrderId(), order);

        future.whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("Failed to send order {}: {}", order.getOrderId(), ex.getMessage());
            } else {
                log.info("Order {} sent to partition {} with offset {}",
                        order.getOrderId(),
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset());
            }
        });
    }
}

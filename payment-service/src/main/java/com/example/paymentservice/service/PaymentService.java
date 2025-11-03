package com.example.paymentservice.service;

import com.example.common.events.OrderCreatedEvent;
import com.example.common.events.PaymentProcessedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final KafkaTemplate<String, PaymentProcessedEvent> kafkaTemplate;

    @Value("${app.kafka.topics.payments-processed}")
    private String paymentsProcessedTopic;

    @KafkaListener(topics = "${app.kafka.topics.orders-created}", containerFactory = "orderListenerContainerFactory")
    public void handleOrderCreated(OrderCreatedEvent event) {
        log.info("Received order created event: {}", event);

        // Simulate payment processing
        boolean paymentSuccessful = true;

        publishPaymentResult(event.getOrderId(), paymentSuccessful);
    }

    public void publishPaymentResult(String orderId, boolean success) {
        PaymentProcessedEvent event = PaymentProcessedEvent.builder()
                .orderId(orderId)
                .successful(success)
                .build();

        kafkaTemplate.send(paymentsProcessedTopic, orderId, event);
        log.info("Published payment processed event for orderId: {}", orderId);
    }
}

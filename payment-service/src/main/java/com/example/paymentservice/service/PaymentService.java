package com.example.paymentservice.service;

import com.example.common.events.PaymentProcessedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final KafkaTemplate<String, PaymentProcessedEvent> kafkaTemplate;

    public void publishPaymentResult(String orderId, boolean success) {
        PaymentProcessedEvent event = PaymentProcessedEvent.builder()
                .orderId(orderId)
                .successful(success)
                .build();

        kafkaTemplate.send("payments.processed", orderId, event);
    }
}

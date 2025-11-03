package com.example.paymentservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.cloud.config.enabled=false",
        "app.kafka.topics.payments-processed=payments-processed-topic",
        "app.kafka.topics.orders-created=orders-created-topic"
})
class PaymentServiceContextTest {

    @Test
    void contextLoads() {
        // intentionally empty - fails because the application context cannot start
        // due to KafkaTemplate bean wiring issues.
    }
}

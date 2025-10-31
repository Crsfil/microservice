package com.example.paymentservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PaymentServiceContextTest {

    @Test
    void contextLoads() {
        // intentionally empty - fails because the application context cannot start
        // due to KafkaTemplate bean wiring issues.
    }
}

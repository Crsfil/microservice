package com.example.orderservice;

import com.example.common.dto.CreateOrderRequest;
import com.example.common.events.OrderCreatedEvent;
import com.example.orderservice.domain.OrderRepository;
import com.example.orderservice.service.OrderService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest
class OrderServiceTransactionalTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @MockBean
    private KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

    @Test
    void shouldRollbackWhenKafkaSendFails() {
        Mockito.when(kafkaTemplate.send(anyString(), anyString(), any(OrderCreatedEvent.class)))
                .thenThrow(new IllegalStateException("Kafka is unavailable"));

        CreateOrderRequest request = new CreateOrderRequest("demo-sku", 2);

        assertThatThrownBy(() -> orderService.createOrder(request))
                .isInstanceOf(IllegalStateException.class);

        assertThat(orderRepository.count())
                .as("order should not be persisted if Kafka publish fails")
                .isZero();
    }
}

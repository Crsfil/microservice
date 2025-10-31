package com.example.orderservice.service;

import com.example.common.dto.CreateOrderRequest;
import com.example.common.events.OrderCreatedEvent;
import com.example.common.model.OrderStatus;
import com.example.orderservice.domain.OrderEntity;
import com.example.orderservice.domain.OrderRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

    @Value("${app.kafka.topics.orders-created}")
    private String ordersCreatedTopic;

    @Transactional
    public OrderEntity createOrder(CreateOrderRequest request) {
        OrderEntity order = new OrderEntity();
        order.setSku(request.sku());
        order.setQuantity(request.quantity());
        order.setStatus(OrderStatus.NEW);

        OrderEntity saved = orderRepository.save(order);

        OrderCreatedEvent event = OrderCreatedEvent.builder()
                .orderId(saved.getId().toString())
                .sku(saved.getSku())
                .quantity(saved.getQuantity())
                .build();

        try {
            kafkaTemplate.send(ordersCreatedTopic, event.getOrderId(), event).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        return saved;
    }
}

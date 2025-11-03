package com.example.orderservice.controller;

import com.example.common.dto.CreateOrderRequest;
import com.example.orderservice.controller.dto.OrderResponse;
import com.example.orderservice.domain.OrderEntity;
import com.example.orderservice.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        OrderEntity order = orderService.createOrder(request);
        return ResponseEntity.ok(OrderResponse.fromEntity(order));
    }
}

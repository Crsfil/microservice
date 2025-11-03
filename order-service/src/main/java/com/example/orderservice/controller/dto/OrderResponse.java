package com.example.orderservice.controller.dto;

import com.example.common.model.OrderStatus;
import com.example.orderservice.domain.OrderEntity;

public record OrderResponse(
        String id,
        String sku,
        int quantity,
        OrderStatus status
) {
    public static OrderResponse fromEntity(OrderEntity e) {
        return new OrderResponse(
                e.getId() != null ? e.getId().toString() : null,
                e.getSku(),
                e.getQuantity(),
                e.getStatus()
        );
    }
}


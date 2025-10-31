package com.example.common.events;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class OrderCreatedEvent {
    String orderId;
    String sku;
    int quantity;
}

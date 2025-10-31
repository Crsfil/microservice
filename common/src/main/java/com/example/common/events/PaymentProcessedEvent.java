package com.example.common.events;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PaymentProcessedEvent {
    String orderId;
    boolean successful;
}

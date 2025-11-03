package com.example.inventoryservice.controller.dto;

import com.example.inventoryservice.domain.InventoryItem;

public record InventoryItemResponse(
        String id,
        String sku,
        int availableQuantity
) {
    public static InventoryItemResponse fromEntity(InventoryItem e) {
        return new InventoryItemResponse(
                e.getId() != null ? e.getId().toString() : null,
                e.getSku(),
                e.getAvailableQuantity()
        );
    }
}


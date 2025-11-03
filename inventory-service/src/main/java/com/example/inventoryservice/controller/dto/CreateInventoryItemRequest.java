package com.example.inventoryservice.controller.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CreateInventoryItemRequest(
        @NotBlank String sku,
        @Min(1) int quantity
) {}

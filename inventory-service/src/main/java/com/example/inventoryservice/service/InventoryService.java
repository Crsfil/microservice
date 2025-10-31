package com.example.inventoryservice.service;

import com.example.inventoryservice.domain.InventoryItem;
import com.example.inventoryservice.domain.InventoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryItem createItem(String sku, int quantity) {
        inventoryRepository.findBySku(sku).ifPresent(item -> {
            throw new IllegalArgumentException("SKU already exists: " + sku);
        });
        InventoryItem item = new InventoryItem(sku, quantity);
        return inventoryRepository.save(item);
    }

    @Transactional(readOnly = true)
    public InventoryItem getBySku(String sku) {
        return inventoryRepository.findBySku(sku)
                .orElseThrow(() -> new EntityNotFoundException("No inventory for SKU " + sku));
    }

    public void reserve(String sku, int quantity) {
        InventoryItem item = getBySku(sku);

        if (item.getAvailableQuantity() < quantity) {
            throw new IllegalArgumentException("Insufficient stock for " + sku);
        }

        item.setAvailableQuantity(item.getAvailableQuantity() - quantity);
        inventoryRepository.save(item);
    }
}

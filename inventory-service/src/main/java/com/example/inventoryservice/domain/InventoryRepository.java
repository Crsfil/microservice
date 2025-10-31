package com.example.inventoryservice.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface InventoryRepository extends JpaRepository<InventoryItem, UUID> {
    Optional<InventoryItem> findBySku(String sku);
}

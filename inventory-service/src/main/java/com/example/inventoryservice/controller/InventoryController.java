package com.example.inventoryservice.controller;

import com.example.inventoryservice.controller.dto.ReserveStockRequest;
import com.example.inventoryservice.domain.InventoryItem;
import com.example.inventoryservice.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping
    public ResponseEntity<InventoryItem> create(@Valid @RequestBody ReserveStockRequest request) {
        InventoryItem item = inventoryService.createItem(request.sku(), request.quantity());
        return ResponseEntity.ok(item);
    }

    @PostMapping("/reserve")
    public ResponseEntity<Void> reserve(@Valid @RequestBody ReserveStockRequest request) {
        inventoryService.reserve(request.sku(), request.quantity());
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/{sku}")
    public ResponseEntity<InventoryItem> find(@PathVariable String sku) {
        return ResponseEntity.ok(inventoryService.getBySku(sku));
    }
}

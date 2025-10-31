package com.example.inventoryservice;

import com.example.inventoryservice.domain.InventoryItem;
import com.example.inventoryservice.domain.InventoryRepository;
import com.example.inventoryservice.service.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class InventoryServiceReservationTest {

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private InventoryRepository inventoryRepository;

    @BeforeEach
    void clean() {
        inventoryRepository.deleteAll();
    }

    @Test
    void reserveShouldReduceQuantity() {
        InventoryItem item = inventoryService.createItem("spoon", 5);

        inventoryService.reserve(item.getSku(), 3);

        InventoryItem updated = inventoryRepository.findBySku(item.getSku())
                .orElseThrow();

        assertThat(updated.getAvailableQuantity())
                .as("reserve should subtract requested quantity")
                .isEqualTo(2);
    }
}

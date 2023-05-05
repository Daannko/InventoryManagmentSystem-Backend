package com.example.InventoryManagmentSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data@Builder
@AllArgsConstructor
public class ItemResponse {
    private ProductDto productDto;
    private int quantity;
}

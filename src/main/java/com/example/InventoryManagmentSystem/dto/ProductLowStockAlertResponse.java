package com.example.InventoryManagmentSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ProductLowStockAlertResponse {

    private ProductDto product;
    private String storehouseName;
    private int amount;
}

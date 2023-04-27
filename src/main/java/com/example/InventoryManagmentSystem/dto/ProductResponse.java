package com.example.InventoryManagmentSystem.dto;

import com.example.InventoryManagmentSystem.models.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private ProductDto product;
    private int quantity;
    private String message;

}

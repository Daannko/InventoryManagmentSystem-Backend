package com.example.InventoryManagmentSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private double price;
    private Long categoryId;
    private String category;
    private String manufacturer;
    private boolean isAvailable;
    private String url;
}

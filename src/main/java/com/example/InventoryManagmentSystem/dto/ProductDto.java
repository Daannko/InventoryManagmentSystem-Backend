package com.example.InventoryManagmentSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductDto {
    private String name;
    private String description;
    private double price;
    private Long categoryId;
    private String manufacturer;
}

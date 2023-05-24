package com.example.InventoryManagmentSystem.dto;

import lombok.Data;

@Data
public class CategoryAddRequest {
    private String name;
    private Long parentId;

}

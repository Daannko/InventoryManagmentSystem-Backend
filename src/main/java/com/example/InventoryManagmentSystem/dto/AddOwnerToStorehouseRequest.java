package com.example.InventoryManagmentSystem.dto;

import lombok.Data;

@Data
public class AddOwnerToStorehouseRequest {
    private String userEmail;
    private Long storehouseId;
}

package com.example.InventoryManagmentSystem.dto;

import lombok.Data;

@Data
public class AddOwnerToStorehouseRequest {

    private Long userId;
    private Long storehouseId;
}

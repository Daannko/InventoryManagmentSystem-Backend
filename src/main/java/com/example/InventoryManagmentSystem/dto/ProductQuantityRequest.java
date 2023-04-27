package com.example.InventoryManagmentSystem.dto;

import lombok.Data;

@Data
public class ProductQuantityRequest {

    private Long storehouseId;
    private Long productId;
    private int quantity;

}

package com.example.InventoryManagmentSystem.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductQuantityRequest {

    private Long storehouseId;
    private Long productId;
    private int quantity;
    private Boolean blocked;

}

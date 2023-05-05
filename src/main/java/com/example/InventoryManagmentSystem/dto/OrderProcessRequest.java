package com.example.InventoryManagmentSystem.dto;

import com.example.InventoryManagmentSystem.models.OrderStatus;
import com.example.InventoryManagmentSystem.models.Storehouse;
import lombok.Data;

@Data
public class OrderProcessRequest {

    private Long orderId;
    private Long userId;
    private OrderStatus orderStatus;

}

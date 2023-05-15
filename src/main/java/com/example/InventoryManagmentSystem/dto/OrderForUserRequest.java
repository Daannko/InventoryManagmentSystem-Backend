package com.example.InventoryManagmentSystem.dto;

import com.example.InventoryManagmentSystem.models.OrderStatus;
import lombok.Data;

@Data
public class OrderForUserRequest {
    private OrderStatus orderStatus;
}

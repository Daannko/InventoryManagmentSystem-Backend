package com.example.InventoryManagmentSystem.dto;

import com.example.InventoryManagmentSystem.models.Item;
import com.example.InventoryManagmentSystem.models.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.util.List;
@Data
@Builder
public class OrderResponse {
    private Long orderId;
    private Long userId;
    private Long formStorehouseId;
    private Long toStorehouseId;
    private List<ItemResponse> items;
    private OrderStatus status;
    private String message;
}
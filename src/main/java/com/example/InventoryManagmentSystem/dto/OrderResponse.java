package com.example.InventoryManagmentSystem.dto;

import com.example.InventoryManagmentSystem.models.Item;
import com.example.InventoryManagmentSystem.models.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;
@Data
@Builder
public class OrderResponse {
    private Long orderId;
    private String userName;
    private Long formStorehouseId;
    private Long toStorehouseId;
    private Date createdAt;
    private Date statusChangedAt;
    private List<ItemResponse> items;
    private OrderStatus status;
    private String message;
}

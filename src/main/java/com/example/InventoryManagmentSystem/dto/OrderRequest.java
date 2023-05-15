package com.example.InventoryManagmentSystem.dto;

import com.example.InventoryManagmentSystem.models.Item;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class OrderRequest {
    private Long formStorehouseId;
    private Long toStorehouseId;
    private List<Item> items;
}

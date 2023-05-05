package com.example.InventoryManagmentSystem.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderForUserResponse {

    private List<OrderResponse> orderedToYourStorehouses;
    private List<OrderResponse> orderedFromYourStorehouses;
}

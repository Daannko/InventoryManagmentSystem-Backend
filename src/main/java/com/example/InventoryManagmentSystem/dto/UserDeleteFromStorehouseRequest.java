package com.example.InventoryManagmentSystem.dto;

import lombok.Data;

@Data
public class UserDeleteFromStorehouseRequest {

    private String userEmail;
    private Long storehouseId;
    private String confirmationPassword;

}

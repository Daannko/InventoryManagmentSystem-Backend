package com.example.InventoryManagmentSystem.dto;

import lombok.Data;

@Data
public class UserDeleteFromCompanyRequest {

    private String userEmail;
    private String confirmationPassword;

}

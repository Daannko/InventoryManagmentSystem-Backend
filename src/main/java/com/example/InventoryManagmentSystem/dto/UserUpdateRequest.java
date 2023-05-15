package com.example.InventoryManagmentSystem.dto;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private String firstname;
    private String surname;
    private String email;
    private String password;

    private String confirmPassword;
}

package com.example.InventoryManagmentSystem.dto;

import com.example.InventoryManagmentSystem.models.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class LoginResponse {
    private final String accessToken;
    private final Long id;
    private final String email;
    private final List<Role> roles;
    private final String name;
    private final String surname;
    private final String message;
}

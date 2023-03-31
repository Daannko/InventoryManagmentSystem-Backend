package com.example.InventoryManagmentSystem.dto;

import com.example.InventoryManagmentSystem.models.Role;
import lombok.Data;

import java.util.List;

@Data
public class UserResponse {
    private long id;
    private String email;
    private List<Role> roles;
    private String name;
    private String surname;
    private boolean verified;
}

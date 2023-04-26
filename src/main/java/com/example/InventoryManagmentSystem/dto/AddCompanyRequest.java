package com.example.InventoryManagmentSystem.dto;

import com.example.InventoryManagmentSystem.models.User;
import lombok.Data;

import java.util.List;

@Data
public class AddCompanyRequest {
    private String name;
    private Long owner;
}

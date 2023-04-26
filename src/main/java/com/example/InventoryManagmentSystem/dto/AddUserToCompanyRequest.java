package com.example.InventoryManagmentSystem.dto;

import lombok.Data;

@Data
public class AddUserToCompanyRequest {
    private Long userId;
    private Long companyId;
}

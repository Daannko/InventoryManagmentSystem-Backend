package com.example.InventoryManagmentSystem.dto;

import com.example.InventoryManagmentSystem.models.Company;
import com.example.InventoryManagmentSystem.models.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CompanyResponse {
    private Long id;
    private String name;
    private List<UserResponse> employees;
    private String message;

    public CompanyResponse(String message){
        this.message = message;
    }
}



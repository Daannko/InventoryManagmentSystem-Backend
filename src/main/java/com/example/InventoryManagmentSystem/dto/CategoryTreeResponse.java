package com.example.InventoryManagmentSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryTreeResponse {
    Long id;
    String name;
    List<CategoryTreeResponse> node;

}

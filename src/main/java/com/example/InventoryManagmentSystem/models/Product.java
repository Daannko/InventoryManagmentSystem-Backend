package com.example.InventoryManagmentSystem.models;

import com.example.InventoryManagmentSystem.dto.ProductDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name = "products")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;
    private String description;
    private double price;
    private Long categoryId;
    private boolean isAvailable;
    private String url;
    private String manufacturer;
    public ProductDto dto(String category){
        return new ProductDto(
                this.id,
                this.name,
                this.description,
                this.price,
                this.categoryId,
                category,
                this.manufacturer,
        this.isAvailable);

    }


}

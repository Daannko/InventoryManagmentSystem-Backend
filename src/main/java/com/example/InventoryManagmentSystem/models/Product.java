package com.example.InventoryManagmentSystem.models;

import com.example.InventoryManagmentSystem.dto.ProductDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

import javax.persistence.*;

@Entity
@Data
@Table(name = "products")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
    private String name;
    private String description;
    private double price;
    private Long categoryId;
    private boolean isAvailable;
    private String manufacturer;
    public ProductDto dto(){
        return new ProductDto(
                this.name,
                this.description,
                this.price,
                this.categoryId,
                this.manufacturer);
    }

}

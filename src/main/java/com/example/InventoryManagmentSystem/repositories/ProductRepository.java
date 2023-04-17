package com.example.InventoryManagmentSystem.repositories;

import com.example.InventoryManagmentSystem.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {
    List<Product> findAll();
}

package com.example.InventoryManagmentSystem.services;

import com.example.InventoryManagmentSystem.models.Product;
import com.example.InventoryManagmentSystem.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    public Product addProduct(Product product){
        return productRepository.save(product);
    }

}

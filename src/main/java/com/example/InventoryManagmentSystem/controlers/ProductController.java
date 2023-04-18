package com.example.InventoryManagmentSystem.controlers;

import com.example.InventoryManagmentSystem.models.Product;
import com.example.InventoryManagmentSystem.services.ProductService;
import com.example.InventoryManagmentSystem.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("product")
public class ProductController{

    private final ProductService productService;

    @GetMapping("/all")
    public List<Product> getAllProducts(){
        return productService.getAllProducts();
    }

    @PostMapping("/add")
    public Product add(@RequestBody Product product){
        return productService.addProduct(product);
    }

}

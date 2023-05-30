package com.example.InventoryManagmentSystem.controlers;

import com.example.InventoryManagmentSystem.dto.ProductDto;
import com.example.InventoryManagmentSystem.models.Product;
import com.example.InventoryManagmentSystem.models.Storehouse;
import com.example.InventoryManagmentSystem.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("product")
public class ProductController{

    private final ProductService productService;

    @GetMapping("/all")
    public List<ProductDto> getAllProducts(){
        return productService.getAllProducts();
    }

    @PostMapping("/add")
    public Product add(@RequestBody Product product){
        return productService.addProduct(product);
    }

    @GetMapping("/storehouses/{id}")
    public List<Storehouse> getStoreghouses(@PathVariable Long id){
        return productService.getStorehousesWithProduct(id);
    }

    @GetMapping("/{id}")
    public ProductDto getById(@PathVariable Long id){
        return productService.getProductById(id);
    }
}

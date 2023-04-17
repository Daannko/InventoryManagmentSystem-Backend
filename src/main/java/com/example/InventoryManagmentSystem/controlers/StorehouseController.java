package com.example.InventoryManagmentSystem.controlers;
import com.example.InventoryManagmentSystem.models.Product;
import com.example.InventoryManagmentSystem.models.Storehouse;
import com.example.InventoryManagmentSystem.services.ProductService;
import com.example.InventoryManagmentSystem.services.StorehouseService;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.Store;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("storehouse")
public class StorehouseController {

    private final StorehouseService storehouseService;

    @GetMapping("/all")
    public List<Storehouse> getAllProducts(){
        return storehouseService.getAllStorehouses();
    }

    @PostMapping("/add")
    public Storehouse add(@RequestBody Storehouse storehouse){
        return storehouseService.addProduct(storehouse);
    }

}

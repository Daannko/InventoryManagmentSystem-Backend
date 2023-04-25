package com.example.InventoryManagmentSystem.controlers;
import com.example.InventoryManagmentSystem.dto.AddOwnerToStorehouseRequest;
import com.example.InventoryManagmentSystem.models.Storehouse;
import com.example.InventoryManagmentSystem.services.StorehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public Storehouse add(@RequestBody Storehouse request){
        return storehouseService.addStorehouse(request);
    }
    @PostMapping("/user")
    public ResponseEntity <?> add(@RequestBody AddOwnerToStorehouseRequest request){
        return ResponseEntity.ok(storehouseService.addOwnerToStorehouse(request));
    }

}

package com.example.InventoryManagmentSystem.controlers;

import com.example.InventoryManagmentSystem.dto.CategoryAddRequest;
import com.example.InventoryManagmentSystem.dto.CategoryGetItemsByCategory;
import com.example.InventoryManagmentSystem.dto.CategoryTreeResponse;
import com.example.InventoryManagmentSystem.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;
    @PostMapping("/add")
    public ResponseEntity<?> addCategory(@RequestBody CategoryAddRequest request){
        return ResponseEntity.ok(categoryService.add(request));
    }

    @GetMapping("/tree")
    public ResponseEntity<?> getCategoryTree(){
        return ResponseEntity.ok( categoryService.getCateogryTree());
    }

    @PostMapping("/get")
    public ResponseEntity<?> getCategoryTree(@RequestBody CategoryGetItemsByCategory request){
        return ResponseEntity.ok(categoryService.getItems(request));
    }


}

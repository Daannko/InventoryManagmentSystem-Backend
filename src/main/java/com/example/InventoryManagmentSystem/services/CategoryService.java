package com.example.InventoryManagmentSystem.services;

import com.example.InventoryManagmentSystem.dto.*;
import com.example.InventoryManagmentSystem.models.Category;
import com.example.InventoryManagmentSystem.models.Item;
import com.example.InventoryManagmentSystem.models.Product;
import com.example.InventoryManagmentSystem.repositories.CategoryRepository;
import com.example.InventoryManagmentSystem.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;
    private final StorehouseService storehouseService;

    public Category add(CategoryAddRequest request){
        Category category = new Category();
        category.setName(request.getName());
        category.setParentId(request.getParentId());

        System.out.println(category.getName());

        return categoryRepository.save(category);
    }


    public List<CategoryTreeResponse> getCateogryTree() {

        CategoryTreeResponse categoryTreeResponse = new CategoryTreeResponse();
        List<Category> optional = categoryRepository.findAllByParentIdNull();

        List<CategoryTreeResponse> categoryTreeResponseList = new ArrayList<>();
        for(Category c : optional){
            CategoryTreeResponse ctr = new CategoryTreeResponse();
            ctr.setName(c.getName());
            ctr.setId(c.getId());
            categoryTreeResponseList.add(ctr);
        }

        for(CategoryTreeResponse c : categoryTreeResponseList){
            List<CategoryTreeResponse> ctrl = new ArrayList<>();
            for(Category cat :  categoryRepository.findAllByParentId(c.getId())){
                ctrl.add(new CategoryTreeResponse(cat.getId(),cat.getName(),null));
            }
            c.setNode(ctrl);
        }
        return categoryTreeResponseList;
    }

    public List<ProductDto> getItems(CategoryGetItemsByCategory request) {

        List<Long> ids = new ArrayList<>();
        ids.add(request.getCategoryId());
        ids.addAll(categoryRepository.findAllByParentId(request.getCategoryId()).stream().map(Category::getId).collect(Collectors.toList()));

        List<Product> allProducts = new ArrayList<>();
        for(Long id : ids){
            allProducts.addAll(productRepository.findAllByCategoryId(id));
        }

        if(request.getStorehouseId() != null){
            List<Long> productResponses = storehouseService.getStorehouseInventory(request.getStorehouseId())
                    .stream()
                    .map(e-> e.getProduct().getId())
                    .collect(Collectors.toList());
            allProducts = allProducts.stream().filter(e -> productResponses.contains(e.getId())).collect(Collectors.toList());
        }

        return allProducts.stream().map(productService::changeProductToCategory).collect(Collectors.toList());
    }
}

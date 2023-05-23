package com.example.InventoryManagmentSystem.services;

import com.example.InventoryManagmentSystem.dto.ProductDto;
import com.example.InventoryManagmentSystem.dto.ProductLowStockAlertResponse;
import com.example.InventoryManagmentSystem.dto.ProductQuantityRequest;
import com.example.InventoryManagmentSystem.dto.ProductResponse;
import com.example.InventoryManagmentSystem.models.*;
import com.example.InventoryManagmentSystem.repositories.CategoryRepository;
import com.example.InventoryManagmentSystem.repositories.ProductRepository;
import com.example.InventoryManagmentSystem.repositories.QuantityRepository;
import com.example.InventoryManagmentSystem.repositories.StorehouseRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final QuantityRepository quantityRepository;
    private final StorehouseRepository storehouseRepository;
    private final CategoryRepository categoryRepository;
    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    public Product addProduct(Product product){
        return productRepository.save(product);
    }

    public Boolean checkProductsQuantity(Long storehouseId,List<Item> items){
        Optional<List<Quantity>> optionalQuantities = quantityRepository.findAllByStorehouseId(storehouseId);
        if(optionalQuantities.isEmpty()){
            return false;
        }

        List<Long> neededProductsIds = items.stream().map(Item::getProductId).collect(Collectors.toList());
        List<Quantity> quantities = optionalQuantities.get().stream().filter(e -> neededProductsIds.contains(e.getProductId())).collect(Collectors.toList());

        for (Quantity quantity : quantities) {
            if (quantity.getQuantity() < items.stream().filter(e -> e.getProductId().equals(quantity.getProductId())).collect(Collectors.toList()).get(0).getQuantity()) {
                return false;
            }
        }
        return true;
    }
    public ProductResponse changeProductQuantity(ProductQuantityRequest request){

        if(storehouseRepository.findById(request.getStorehouseId()).isEmpty() || productRepository.findById(request.getProductId()).isEmpty()){
            return ProductResponse.builder()
                    .message("Can't find storehouse with ID : " + request.getProductId() + " or product with ID: " + request.getProductId()).build();
        }

        Quantity quantity;
        Optional<Quantity> optionalQuantity = quantityRepository.findByStorehouseIdAndProductId(request.getStorehouseId(), request.getProductId());
        if(optionalQuantity.isEmpty()){
            quantity = Quantity.builder()
                    .storehouseId(request.getStorehouseId())
                    .blocked(request.getBlocked() == null ? true : request.getBlocked())
                    .productId(request.getProductId())
                    .quantity(request.getQuantity())
                    .build();
        } else {
            quantity = optionalQuantity.get();
            quantity.setQuantity(quantity.getQuantity() + request.getQuantity());
        }

        if(quantity.getQuantity() < 0){
            return ProductResponse.builder()
                    .message("Not enough resources for item with ID: " + request.getProductId()).build();
        }

        quantityRepository.save(quantity);

        return ProductResponse.builder()
                .message("Operation succeeded")
                .product(changeProductToCategory(productRepository.getById(quantity.getProductId())))
                .quantity(quantity.getQuantity())
                .build();
    }

    public ProductDto changeProductToCategory(Product product){
        String category = "";
        Optional<Category> optionalCategory = categoryRepository.findById(product.getCategoryId());
        if(optionalCategory.isPresent()) category = optionalCategory.get().getName();
        return product.dto(category);
    }

}

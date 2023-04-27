package com.example.InventoryManagmentSystem.services;

import com.example.InventoryManagmentSystem.dto.ProductQuantityRequest;
import com.example.InventoryManagmentSystem.dto.ProductResponse;
import com.example.InventoryManagmentSystem.models.Product;
import com.example.InventoryManagmentSystem.models.Quantity;
import com.example.InventoryManagmentSystem.repositories.ProductRepository;
import com.example.InventoryManagmentSystem.repositories.QuantityRepository;
import com.example.InventoryManagmentSystem.repositories.StorehouseRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.List;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final QuantityRepository quantityRepository;
    private final StorehouseRepository storehouseRepository;

    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    public Product addProduct(Product product){
        return productRepository.save(product);
    }

    public ProductResponse changeProductQuantity(ProductQuantityRequest request){

        if(storehouseRepository.findById(request.getStorehouseId()).isEmpty() || productRepository.findById(request.getProductId()).isEmpty()){
            return ProductResponse.builder()
                    .message("Can't find storehouse or product :(").build();
        }

        Quantity quantity;
        Optional<Quantity> optionalQuantity = quantityRepository.findByStorehouseIdAndProductId(request.getStorehouseId(), request.getProductId());
        if(optionalQuantity.isEmpty()){
            quantity = Quantity.builder()
                    .storehouseId(request.getStorehouseId())
                    .blocked(false)
                    .productId(request.getProductId())
                    .quantity(request.getQuantity())
                    .build();
        } else {
            quantity = optionalQuantity.get();
            quantity.setQuantity(quantity.getQuantity() + request.getQuantity());
        }

        if(quantity.getQuantity() < 0){
            return ProductResponse.builder()
                    .message("Not enough resources").build();
        }

        quantityRepository.save(quantity);

        return ProductResponse.builder()
                .message("Operation succeeded")
                .product(productRepository.getById(quantity.getProductId()).dto())
                .quantity(quantity.getQuantity())
                .build();
    }

}

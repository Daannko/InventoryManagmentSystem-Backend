package com.example.InventoryManagmentSystem.services;

import com.example.InventoryManagmentSystem.dto.*;
import com.example.InventoryManagmentSystem.models.*;
import com.example.InventoryManagmentSystem.repositories.CategoryRepository;
import com.example.InventoryManagmentSystem.repositories.ProductRepository;
import com.example.InventoryManagmentSystem.repositories.QuantityRepository;
import com.example.InventoryManagmentSystem.repositories.StorehouseRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;
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
    public List<ProductDto> getAllProducts(){
        List<ProductDto> products = productRepository.findAll()
                .stream().map(e -> e.dto("")).collect(Collectors.toList());
        for(ProductDto product : products){
            Optional <Category> cat = categoryRepository.findById(product.getCategoryId());
            cat.ifPresent(category -> product.setCategory(category.getName()));
        }
        return products;
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

    public List<Storehouse> getStorehousesWithProduct(Long id){
        Optional<List<Quantity>> quantityOptional =  quantityRepository.findAllByProductId(id);
        List<Storehouse> storehouses = new ArrayList<>();
        if(quantityOptional.isPresent()){
            for(Quantity quantity : quantityOptional.get()){
                Storehouse storehouse = storehouseRepository.findById(quantity.getStorehouseId()).orElse(null);
                if(storehouse != null && storehouse.isBig()){
                    storehouses.add(storehouse);
                }
            }
        }
        return storehouses;
    }

    public ProductDto getProductById(Long id){
        return changeProductToCategory(Objects.requireNonNull(productRepository.findById(id).orElse(null)));
    }

}

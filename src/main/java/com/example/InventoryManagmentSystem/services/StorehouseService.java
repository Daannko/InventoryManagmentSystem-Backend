package com.example.InventoryManagmentSystem.services;

import com.example.InventoryManagmentSystem.dto.AddOwnerToStorehouseRequest;
import com.example.InventoryManagmentSystem.dto.MessageResponse;
import com.example.InventoryManagmentSystem.dto.ProductResponse;
import com.example.InventoryManagmentSystem.models.Product;
import com.example.InventoryManagmentSystem.models.Quantity;
import com.example.InventoryManagmentSystem.models.Storehouse;
import com.example.InventoryManagmentSystem.models.User;
import com.example.InventoryManagmentSystem.repositories.ProductRepository;
import com.example.InventoryManagmentSystem.repositories.QuantityRepository;
import com.example.InventoryManagmentSystem.repositories.StorehouseRepository;
import com.example.InventoryManagmentSystem.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.apache.catalina.Store;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StorehouseService {

    private final StorehouseRepository storehouseRepository;
    private final UserRepository userRepository;
    private final QuantityRepository quantityRepository;
    private final ProductRepository productRepository;

    public List<Storehouse> getAllStorehouses(){
        return storehouseRepository.findAll();
    }

    public Storehouse addStorehouse(Storehouse storehouse){
        return storehouseRepository.save(storehouse);
    }

    public MessageResponse addOwnerToStorehouse(AddOwnerToStorehouseRequest request){

        Optional<Storehouse> optionalStorehouse = storehouseRepository.findById(request.getStorehouseId());
        Optional<User> optionalUser = userRepository.findById(request.getUserId());
        if(optionalStorehouse.isEmpty() || optionalUser.isEmpty()){
            return new MessageResponse("Could not find a user or storehouse :(");
        }

        User user = optionalUser.get();
        Storehouse storehouse = optionalStorehouse.get();

        if(user.getManagedStorehouses().stream().filter(e -> Objects.equals(e.getId(), storehouse.getId())).toArray().length != 0){
            return new MessageResponse("User already manages the shop");
        }

        storehouse.getOwners().add(user);
        user.getManagedStorehouses().add(storehouse);
        storehouseRepository.save(storehouse);
        userRepository.save(user);

        return new MessageResponse("Adding user succeeded");
    }
    public List<ProductResponse> getStorehouseInventory(Long id){

        Optional<Storehouse> optionalStorehouse = storehouseRepository.findById(id);
        if(optionalStorehouse.isEmpty()){
            return Collections.singletonList(ProductResponse.builder().message("No storage found").build());
        }
        if(quantityRepository.findAllByStorehouseId(id).isEmpty()){
            return Collections.singletonList(ProductResponse.builder().message("No items found").build());
        }
        List<ProductResponse> list = new ArrayList<>();
        for(Quantity q : quantityRepository.findAllByStorehouseId(id).get()){
            ProductResponse productResponse = new ProductResponse();
            productResponse.setProduct(productRepository.getById(q.getProductId()).dto());
            productResponse.setQuantity(q.getQuantity());
            productResponse.setMessage("");
            list.add(productResponse);
        }
        return list;

    }

}

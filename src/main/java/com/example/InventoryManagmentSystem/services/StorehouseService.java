package com.example.InventoryManagmentSystem.services;

import com.example.InventoryManagmentSystem.dto.AddOwnerToStorehouseRequest;
import com.example.InventoryManagmentSystem.dto.MessageResponse;
import com.example.InventoryManagmentSystem.models.Storehouse;
import com.example.InventoryManagmentSystem.models.User;
import com.example.InventoryManagmentSystem.repositories.StorehouseRepository;
import com.example.InventoryManagmentSystem.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.apache.catalina.Store;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class StorehouseService {

    private final StorehouseRepository storehouseRepository;
    private final UserRepository userRepository;

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

}

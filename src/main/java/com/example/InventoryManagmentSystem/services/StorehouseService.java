package com.example.InventoryManagmentSystem.services;

import com.example.InventoryManagmentSystem.models.Product;
import com.example.InventoryManagmentSystem.models.Storehouse;
import com.example.InventoryManagmentSystem.repositories.StorehouseRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class StorehouseService {

    private final StorehouseRepository storehouseRepository;

    public List<Storehouse> getAllStorehouses(){
        return storehouseRepository.findAll();
    }

    public Storehouse addProduct(Storehouse storehouse){
        return storehouseRepository.save(storehouse);
    }

}

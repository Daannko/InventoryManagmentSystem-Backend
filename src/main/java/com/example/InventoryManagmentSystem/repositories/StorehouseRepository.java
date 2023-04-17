package com.example.InventoryManagmentSystem.repositories;

import com.example.InventoryManagmentSystem.models.Storehouse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StorehouseRepository extends JpaRepository<Storehouse,Long> {
}

package com.example.InventoryManagmentSystem.repositories;

import com.example.InventoryManagmentSystem.models.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
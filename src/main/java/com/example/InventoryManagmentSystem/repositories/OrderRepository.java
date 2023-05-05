package com.example.InventoryManagmentSystem.repositories;

import com.example.InventoryManagmentSystem.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository <Order,Long> {

    List<Order> getByFromStorehouseId(Long id);
    List<Order> getByToStorehouseId(Long id);
}

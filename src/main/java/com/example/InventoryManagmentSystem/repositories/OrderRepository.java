package com.example.InventoryManagmentSystem.repositories;

import com.example.InventoryManagmentSystem.models.Order;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository <Order,Long> {

    List<Order> getByFromStorehouseId(Long id);
    List<Order> getByToStorehouseId(Long id);

    List<Order> findAllByUserId(Long id);

    List<Order> findAllByFromStorehouseId(Long id);
}

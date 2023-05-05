package com.example.InventoryManagmentSystem.repositories;

import com.example.InventoryManagmentSystem.models.Quantity;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuantityRepository extends JpaRepository<Quantity,Long> {
    Optional<Quantity> findByStorehouseIdAndProductId(Long storehouseId,Long productId);
    Optional<List<Quantity>> findAllByStorehouseId(Long id);
    Optional<List<Quantity>> findAllByProductId(Long id);
    Optional<Quantity> findQuantityByStorehouseIdAndProductId(Long storehouseId, Long productId);
}

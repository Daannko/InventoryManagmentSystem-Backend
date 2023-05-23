package com.example.InventoryManagmentSystem.repositories;

import com.example.InventoryManagmentSystem.models.Category;
import com.example.InventoryManagmentSystem.models.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,Long> {

    List<Category> findAllById(Long id);
    List<Category> findAllByParentId(Long id);
    List<Category> findAllByParentIdNull();
    void deleteAll();

}

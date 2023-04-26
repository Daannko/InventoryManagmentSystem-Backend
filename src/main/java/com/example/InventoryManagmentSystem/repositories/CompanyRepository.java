package com.example.InventoryManagmentSystem.repositories;

import com.example.InventoryManagmentSystem.models.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company,Long> {

}

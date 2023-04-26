package com.example.InventoryManagmentSystem.controlers;

import com.example.InventoryManagmentSystem.dto.AddCompanyRequest;
import com.example.InventoryManagmentSystem.dto.AddUserToCompanyRequest;
import com.example.InventoryManagmentSystem.models.Company;
import com.example.InventoryManagmentSystem.services.CompanyService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("company")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping("/add")
    public ResponseEntity<?> addCompany(@RequestBody AddCompanyRequest request){
        return ResponseEntity.ok(companyService.add(request));
    }

    @PostMapping("/employee")
    public ResponseEntity<?> addEmployee(@RequestBody AddUserToCompanyRequest request){
        return ResponseEntity.ok(companyService.addUserToCompany(request));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(companyService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAll(@PathVariable Long id){
        return ResponseEntity.ok(companyService.getById(id));
    }




}

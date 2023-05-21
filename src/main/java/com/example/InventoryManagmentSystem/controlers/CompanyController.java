package com.example.InventoryManagmentSystem.controlers;

import com.example.InventoryManagmentSystem.dto.*;
import com.example.InventoryManagmentSystem.models.Company;
import com.example.InventoryManagmentSystem.repositories.CompanyRepository;
import com.example.InventoryManagmentSystem.services.CompanyService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("company")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping("/add")
    public ResponseEntity<CompanyResponse> addCompany(@RequestBody AddCompanyRequest request){
        CompanyResponse companyResponse;
        try{
            System.out.println("executed");
            companyResponse = companyService.add(request);

        }
        catch (UsernameNotFoundException e){
            return ResponseEntity.ok(new CompanyResponse("User not found"));
        }

        return ResponseEntity.ok(companyResponse);
    }

    @PostMapping("/employee")
    public ResponseEntity<?> addEmployee(@RequestBody AddUserToCompanyRequest request){
        return ResponseEntity.ok(companyService.addUserToCompany(request));
    }

    @PostMapping("/promote")
    public ResponseEntity<?> promoteEmployee(@RequestBody CompanyAdminsChangeRequest request){
        return ResponseEntity.ok(companyService.promoteToAdmin(request));
    }


    @GetMapping("/all")
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(companyService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAll(@PathVariable Long id){
        return ResponseEntity.ok(companyService.getById(id));
    }

    @PostMapping("/employee/remove")
    ResponseEntity<?> deleteUserFromCompany(@RequestBody UserDeleteFromCompanyRequest request){
        return ResponseEntity.ok(companyService.deleteFromCompany(request));
    }


}

package com.example.InventoryManagmentSystem.services;

import com.example.InventoryManagmentSystem.dto.AddCompanyRequest;
import com.example.InventoryManagmentSystem.dto.AddUserToCompanyRequest;
import com.example.InventoryManagmentSystem.dto.MessageResponse;
import com.example.InventoryManagmentSystem.models.Company;
import com.example.InventoryManagmentSystem.models.User;
import com.example.InventoryManagmentSystem.repositories.CompanyRepository;
import com.example.InventoryManagmentSystem.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    public MessageResponse addUserToCompany(AddUserToCompanyRequest request){

        Optional<Company> companyOptional = companyRepository.findById(request.getCompanyId());
        Optional<User> userOptional = userRepository.findById(request.getUserId());

        if(userOptional.isEmpty() || companyOptional.isEmpty()){
            return new MessageResponse("No User or Company found :(");
        }

        if(companyOptional.get().getEmployees().stream().filter(e -> e.getId() == userOptional.get().getId()).toArray().length != 0){
            return new MessageResponse("User is already an employee of a " + companyOptional.get().getName());
        }

        User user = userOptional.get();
        Company company = companyOptional.get();
        company.getEmployees().add(user);
        user.setCompany(company);
        userRepository.save(user);
        companyRepository.save(company);

        return new MessageResponse(user.getName() + " " + user.getSurname() + " was added as a employee to " + company.getName());
    }

    public Company add(AddCompanyRequest request){
        return companyRepository.save(new Company(request.getName(),request.getEmployees()));
    }

    public Company getById(Long id){
        return companyRepository.getById(id);
    }

    public List<Company> getAll(){
        return companyRepository.findAll();
    }

}

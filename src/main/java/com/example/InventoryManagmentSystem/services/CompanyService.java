package com.example.InventoryManagmentSystem.services;

import com.example.InventoryManagmentSystem.dto.AddCompanyRequest;
import com.example.InventoryManagmentSystem.dto.AddUserToCompanyRequest;
import com.example.InventoryManagmentSystem.dto.CompanyResponse;
import com.example.InventoryManagmentSystem.dto.MessageResponse;
import com.example.InventoryManagmentSystem.models.Company;
import com.example.InventoryManagmentSystem.models.User;
import com.example.InventoryManagmentSystem.repositories.CompanyRepository;
import com.example.InventoryManagmentSystem.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

        if(userOptional.get().getCompany() != null){
            return new MessageResponse("User is already an employee of a company");
        }

        User user = userOptional.get();
        Company company = companyOptional.get();
        company.getEmployees().add(user);
        user.setCompany(company);
        userRepository.save(user);

        return new MessageResponse(user.getName() + " " + user.getSurname() + " was added as a employee to " + company.getName());
    }

    public CompanyResponse add(AddCompanyRequest request) throws UsernameNotFoundException{

        Optional<User> optionalUser = userRepository.findById(request.getOwner());
        if(optionalUser.isEmpty()){
            throw new UsernameNotFoundException("User not found");
        }
        if(optionalUser.get().getCompany() != null){
            return new CompanyResponse("User already in company");
        }

        Company company = new Company(request.getName(), Collections.singletonList(optionalUser.get()));

        User user = optionalUser.get();
        user.setCompany(company);
        userRepository.save(user);

        CompanyResponse companyResponse = new CompanyResponse();
        companyResponse.setId(user.getCompany().getId());
        companyResponse.setName(company.getName());
        companyResponse.setEmployees(Collections.singletonList(optionalUser.get().dto()));
        companyResponse.setMessage("Company added!");



        return companyResponse;
    }

    public Company getById(Long id){
        return companyRepository.getById(id);
    }

    public List<CompanyResponse> getAll(){
        return companyRepository.findAll().stream().map(Company::dto).collect(Collectors.toList());
    }

}

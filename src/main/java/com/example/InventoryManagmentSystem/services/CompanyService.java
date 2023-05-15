package com.example.InventoryManagmentSystem.services;

import com.example.InventoryManagmentSystem.dto.*;
import com.example.InventoryManagmentSystem.models.Company;
import com.example.InventoryManagmentSystem.models.Role;
import com.example.InventoryManagmentSystem.models.User;
import com.example.InventoryManagmentSystem.repositories.CompanyRepository;
import com.example.InventoryManagmentSystem.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final UserService userService;


    public MessageResponse addUserToCompany(AddUserToCompanyRequest request){

        User requestUser = userService.getUserFromContext();
        Optional<Company> companyOptional = companyRepository.findById(request.getCompanyId());
        Optional<User> userOptional = userRepository.findById(request.getUserId());

        if(userOptional.isEmpty() || companyOptional.isEmpty()){
            return new MessageResponse("No User or Company found :(");
        }

        User user = userOptional.get();
        Company company = companyOptional.get();

        if(!company.getAdmins().contains(requestUser.getEmail())){
            return new MessageResponse("You dont have rights to do that");
        }

        if(user.getCompany() != null){
            return new MessageResponse("User is already an employee of a company");
        }

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

        User user = optionalUser.get();

        Company company = new Company(request.getName(), Collections.singletonList(optionalUser.get()),user.getEmail());
        user.setCompany(company);
        companyRepository.save(company);
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

    public MessageResponse promoteToAdmin(CompanyAdminsChangeRequest request){
        User requestUser = userService.getUserFromContext();
        User operationalUser = userService.findByEmail(request.getUserEmail());
        Company company = operationalUser.getCompany();

        if(!company.getAdmins().contains(requestUser.getEmail())){
            return new MessageResponse("You don't have role to do that");
        }

        company.getAdmins().add(operationalUser.getEmail());
        companyRepository.save(company);

        return new MessageResponse("Users role was successfuly changed");
    }

    public MessageResponse deleteFromCompany(UserDeleteFromCompanyRequest request){

        User requestUser = userService.getUserFromContext();
        if(request.getUserEmail().equals(requestUser.getEmail()) && !userService.passwordsMatch(request.getConfirmationPassword(),requestUser.getPassword())){
            return new MessageResponse("The confirmation password is incorrect");
        }

        Optional<User> optionalOperationalUser = userRepository.findByEmail(request.getUserEmail());
        if(optionalOperationalUser.isEmpty()){
            return new MessageResponse("There is no user with that email");
        }

        User operationalUser = optionalOperationalUser.get();

        if(operationalUser.getCompany() == null) {
            return new MessageResponse("That user is not part of any company");
        }

        Company company = operationalUser.getCompany();

        if(!requestUser.getRoles().contains(Role.ROLE_ADMIN) && !company.getId().equals(requestUser.getCompany().getId())){
            return new MessageResponse("You can't delete user from company you are not part of");
        }

        operationalUser.setManagedStorehouses(new ArrayList<>());
        operationalUser.setCompany(null);

        company.setAdmins(company.getAdmins().stream().filter(e -> !e.equals(operationalUser.getEmail())).collect(Collectors.toList()));

        companyRepository.save(company);
        userRepository.save(operationalUser);

        return new MessageResponse("User was deleted from company");
    }


}

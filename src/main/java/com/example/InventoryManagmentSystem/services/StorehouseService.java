package com.example.InventoryManagmentSystem.services;

import com.example.InventoryManagmentSystem.dto.AddOwnerToStorehouseRequest;
import com.example.InventoryManagmentSystem.dto.MessageResponse;
import com.example.InventoryManagmentSystem.dto.ProductResponse;
import com.example.InventoryManagmentSystem.dto.UserDeleteFromStorehouseRequest;
import com.example.InventoryManagmentSystem.models.*;
import com.example.InventoryManagmentSystem.repositories.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class StorehouseService {

    private final StorehouseRepository storehouseRepository;
    private final UserRepository userRepository;
    private final QuantityRepository quantityRepository;
    private final ProductRepository productRepository;
    private final CompanyRepository companyRepository;
    private final UserService userService;

    public List<Storehouse> getAllStorehouses(){
        return storehouseRepository.findAll();
    }

    public Storehouse addStorehouse(Storehouse storehouse){
        return storehouseRepository.save(storehouse);
    }

    public MessageResponse addOwnerToStorehouse(AddOwnerToStorehouseRequest request){

        User requestUser = userService.getUserFromContext();
        Optional<Storehouse> optionalStorehouse = storehouseRepository.findById(request.getStorehouseId());
        Optional<User> optionalUser = userRepository.findById(request.getUserId());
        if(optionalStorehouse.isEmpty() || optionalUser.isEmpty()){
            return new MessageResponse("Could not find a user or storehouse :(");
        }

        User user = optionalUser.get();
        Storehouse storehouse = optionalStorehouse.get();

        if(!requestUser.getRoles().contains(Role.ROLE_ADMIN)){
            if(storehouse.getCompanies().stream().filter(e -> Objects.equals(e.getId(), requestUser.getCompany().getId())).toArray().length == 0) {
                return new MessageResponse("Your company dont manage this storehouse");
            }
            if(!requestUser.getCompany().getAdmins().contains(requestUser.getEmail())){
                return new MessageResponse("You dont have a role to do that");
            }
        }

        if(user.getManagedStorehouses().stream().filter(e -> Objects.equals(e.getId(), storehouse.getId())).toArray().length != 0){
            return new MessageResponse("User already manages the shop");
        }

        storehouse.getOwners().add(user);
        user.getManagedStorehouses().add(storehouse);

        Company company = requestUser.getCompany();

        List<Company> companies = storehouse.getCompanies();
        if(companies == null){
            companies = Collections.singletonList(company);
        }
        else{
            companies.add(company);
        }

        List<Storehouse> storehouses = company.getStorehouses();
        if(storehouses == null){
            storehouses = Collections.singletonList(storehouse);
        }
        else{
            storehouses.add(storehouse);
        }

        storehouse.setCompanies(companies);
        company.setStorehouses(storehouses);

        companyRepository.save(company);
        storehouseRepository.save(storehouse);
        userRepository.save(user);

        return new MessageResponse("Adding user succeeded");
    }
    public List<ProductResponse> getStorehouseInventory(Long id){

        Optional<Storehouse> optionalStorehouse = storehouseRepository.findById(id);
        if(optionalStorehouse.isEmpty()){
            return Collections.singletonList(ProductResponse.builder().message("No storage found").build());
        }
        if(quantityRepository.findAllByStorehouseId(id).isEmpty()){
            return Collections.singletonList(ProductResponse.builder().message("No items found").build());
        }
        List<ProductResponse> list = new ArrayList<>();
        for(Quantity q : quantityRepository.findAllByStorehouseId(id).get()){
            ProductResponse productResponse = new ProductResponse();
            productResponse.setProduct(productRepository.getById(q.getProductId()).dto());
            productResponse.setQuantity(q.getQuantity());
            productResponse.setMessage("");
            list.add(productResponse);
        }
        return list;
    }

    public MessageResponse deleteUserFromStorehouse(UserDeleteFromStorehouseRequest request){
        User requestUser = userService.getUserFromContext();
        if(request.getUserEmail().equals(requestUser.getEmail()) && !userService.passwordsMatch(request.getConfirmationPassword(),requestUser.getPassword())){
            return new MessageResponse("The confirmation password is incorrect");
        }

        Optional<User> optionalOperationalUser = userRepository.findByEmail(request.getUserEmail());
        if(optionalOperationalUser.isEmpty()){
            return new MessageResponse("There is no user with that email");
        }

        User operationalUser = optionalOperationalUser.get();
        if(!requestUser.getRoles().contains(Role.ROLE_ADMIN) && !operationalUser.getCompany().getId().equals(requestUser.getCompany().getId())){
            return new MessageResponse("You can't delete user from company you are not part of");
        }

        operationalUser.setManagedStorehouses(new ArrayList<>());
        operationalUser.setCompany(null);
        userRepository.save(operationalUser);

        return new MessageResponse("User was deleted from storehouse");
    }
    public List<Storehouse> getMyStorehouses(){
        return userService.getUserFromContext().getManagedStorehouses();
    }

}

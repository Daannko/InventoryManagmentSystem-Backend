package com.example.InventoryManagmentSystem.services;

import com.example.InventoryManagmentSystem.dto.*;
import com.example.InventoryManagmentSystem.models.*;
import com.example.InventoryManagmentSystem.repositories.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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
        Optional<User> optionalUser = userRepository.findByEmail(request.getUserEmail());

        if(optionalStorehouse.isEmpty() || optionalUser.isEmpty()){
            return new MessageResponse("Could not find a user or storehouse :(");
        }

        User user = optionalUser.get();
        Storehouse storehouse = optionalStorehouse.get();


        if(!requestUser.isAdmin() && requestUser.getCompany() == null){
            return new MessageResponse("You are not in any company");
        }

        if(user.getCompany() == null){
            return new MessageResponse("User is not part of any company");
        }


        if(!requestUser.getRoles().contains(Role.ROLE_ADMIN)){
            if(!requestUser.getCompany().getAdmins().contains(requestUser.getEmail())){
                return new MessageResponse("You dont have a role to do that");
            }
        }

        if(storehouse.getCompanies().stream().noneMatch(e -> e.getId().equals(user.getCompany().getId()))){
            return new MessageResponse("Your company don manage this storehouse");
        }

        if(user.getManagedStorehouses().stream().anyMatch(e -> Objects.equals(e.getId(), storehouse.getId()))){
            return new MessageResponse("User already manages the storehouse");
        }

        storehouse.getOwners().add(user);
        user.getManagedStorehouses().add(storehouse);

        storehouseRepository.save(storehouse);
        userRepository.save(user);

        return new MessageResponse("Adding user succeeded");
    }

    public MessageResponse addCompanyToStorehouse(StorehouseCompanyAddRequest request){

        if(!userService.getUserFromContext().getRoles().contains(Role.ROLE_ADMIN)){
            return new MessageResponse("Only site admin can do that");
        }

        Optional<Company> optionalCompany = companyRepository.findById(request.getCompanyId());
        Optional<Storehouse> optionalStorehouse = storehouseRepository.findById(request.getStorehouseId());

        if(optionalStorehouse.isEmpty() || optionalCompany.isEmpty()){
            return new MessageResponse("Can't find storehouse or company");
        }

        Storehouse storehouse = optionalStorehouse.get();
        Company company = optionalCompany.get();

        if(storehouse.getCompanies().stream().anyMatch(e -> Objects.equals(e.getId(), company.getId()))){
            return new MessageResponse("This company already manages this storehouse");
        }

        storehouse.getCompanies().add(company);
        company.getStorehouses().add(storehouse);

        storehouseRepository.save(storehouse);
        companyRepository.save(company);

        return new MessageResponse("Success");
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
        if(!requestUser.getRoles().contains(Role.ROLE_ADMIN)
                || !operationalUser.getCompany().getId().equals(requestUser.getCompany().getId()))
        {
            return new MessageResponse("You can't delete user from company you are not part of");
        }

        if(!requestUser.getRoles().contains(Role.ROLE_ADMIN) || !requestUser.getCompany().getAdmins().contains(requestUser.getEmail())){
            return new MessageResponse("You dont have permission to do that");
        }

        operationalUser.setManagedStorehouses(operationalUser.getManagedStorehouses().stream().filter(e -> !Objects.equals(e.getId(), request.getStorehouseId())).collect(Collectors.toList()));
        userRepository.save(operationalUser);

        return new MessageResponse("User was deleted from storehouse");
    }
    public List<Storehouse> getMyStorehouses(){
        return userService.getUserFromContext().getManagedStorehouses();
    }

}

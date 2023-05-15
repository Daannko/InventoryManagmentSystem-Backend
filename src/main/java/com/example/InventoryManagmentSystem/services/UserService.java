package com.example.InventoryManagmentSystem.services;

import com.example.InventoryManagmentSystem.dto.MessageResponse;
import com.example.InventoryManagmentSystem.dto.UserDeleteFromCompanyRequest;
import com.example.InventoryManagmentSystem.dto.UserDeleteFromStorehouseRequest;
import com.example.InventoryManagmentSystem.dto.UserUpdateRequest;
import com.example.InventoryManagmentSystem.models.Role;
import com.example.InventoryManagmentSystem.models.User;
import com.example.InventoryManagmentSystem.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public Optional<User> findById(Long id){
        return userRepository.findById(id);
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public User save(User user){
        return userRepository.save(user);
    }

    public void deleteById(Long id){
        userRepository.deleteById(id);
    }

    public User findByEmail(String email) throws UsernameNotFoundException {
        final Optional<User> userOptional = userRepository.findByEmail(email);

        userOptional.orElseThrow(() -> {
            throw new UsernameNotFoundException("Not found " + email);
        });

        return userOptional.get();
    }

    public boolean existsByEmail(String email){
        final Optional<User> userOptional = userRepository.findByEmail(email);

        return userOptional.isPresent();
    }

    public User verifyById(long id){
        Optional<User> userOptional = userRepository.findById(id);

        if(userOptional.isPresent()){
            User user = userOptional.get();
            user.setVerified(true);
            return userRepository.save(user);
        }
        return null;
    }

    public User getUserFromContext(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        return userRepository.findByEmail(username).orElseThrow( () -> new UsernameNotFoundException("User not found"));
    }

    public MessageResponse updateUser(UserUpdateRequest request){

        User user = getUserFromContext();

        if(!passwordsMatch(request.getConfirmPassword(),user.getPassword())){
            return new MessageResponse("The confirmation password is incorrect");
        }

        if(request.getSurname() != null)
            user.setSurname(request.getSurname());
        if(request.getEmail() != null)
            user.setEmail(request.getEmail());
        if(request.getPassword() != null)
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        if(request.getFirstname() != null)
            user.setName(request.getFirstname());


        userRepository.save(user);
        return new MessageResponse("Information changed correctly");

    }

    public boolean passwordsMatch(String confirmationPassword,String usersPassword){
        return passwordEncoder.matches(confirmationPassword,usersPassword);
    }

}

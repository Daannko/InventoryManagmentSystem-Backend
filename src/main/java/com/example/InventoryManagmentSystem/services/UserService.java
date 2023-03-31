package com.example.InventoryManagmentSystem.services;

import com.example.InventoryManagmentSystem.models.User;
import com.example.InventoryManagmentSystem.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;


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

    public User getContextUser() throws UsernameNotFoundException {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final org.springframework.security.core.userdetails.User springUser = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        final Optional<User> userOptional = userRepository.findByEmail(springUser.getUsername());

        userOptional.orElseThrow(() -> {
            throw new UsernameNotFoundException("Not found " + springUser.getUsername());
        });
        return userOptional.get();
    }
}

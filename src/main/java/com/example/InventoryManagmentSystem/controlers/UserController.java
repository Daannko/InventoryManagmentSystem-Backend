package com.example.InventoryManagmentSystem.controlers;

import com.example.InventoryManagmentSystem.models.User;
import com.example.InventoryManagmentSystem.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    ResponseEntity<?> getAll(){
        return ResponseEntity.ok(userService.findAll());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    Optional<User> getById(@PathVariable Long id){
        return userService.findById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/save")
    ResponseEntity<?> addUser(@RequestBody User user){
        return ResponseEntity.ok(userService.save(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update")
    ResponseEntity<?> updateUser(@RequestBody User user){
        return ResponseEntity.ok(userService.save(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    ResponseEntity<?> deleteUser(@PathVariable Long id){
        userService.deleteById(id);
        return ResponseEntity.ok("User deleted");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/verify")
    ResponseEntity<?> verifyById(@PathVariable Long id){
        User user = userService.verifyById(id);
        if(user != null)
            return ResponseEntity.ok(user);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
    }
}

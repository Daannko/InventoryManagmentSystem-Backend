package com.example.SpringBase.configuration;

import com.example.SpringBase.models.*;
import com.example.SpringBase.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;

import static com.example.SpringBase.models.Role.ROLE_ADMIN;
import static com.example.SpringBase.models.Role.ROLE_USER;

@Component
@RequiredArgsConstructor
public class DatabaseLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private User addUser(String email, String password, Role role, String name, String surname) {
        User user = User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .roles(Collections.singletonList(role))
                .name(name)
                .surname(surname)
                //Verification set on true, for testing.
                .verified(true)
                .build();
        this.userRepository.save(user);

        return user;
    }


    @Override
    public void run(String... strings){

        User user1 = addUser("user1@db.com", "user", ROLE_USER, "First", "User");
        User user2 = addUser("user2@db.com", "user", ROLE_USER, "Second", "User");
        User admin = addUser("admin@db.com", "admin", ROLE_ADMIN, "First", "Admin");
    }
}

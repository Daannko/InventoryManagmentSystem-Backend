package com.example.SpringBase.controlers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/hello")
public class HelloController {

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/user")
    ResponseEntity<?> getHelloUser(){
        return ResponseEntity.ok("Hello user!");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    ResponseEntity<?> getHelloAdmin(){
        return ResponseEntity.ok("Hello admin!");
    }

    @GetMapping("/all")
    ResponseEntity<?> getHelloAll(){
        return ResponseEntity.ok("Hello all!");
    }
}
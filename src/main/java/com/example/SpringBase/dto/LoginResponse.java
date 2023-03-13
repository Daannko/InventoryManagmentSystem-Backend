package com.example.SpringBase.dto;

import com.example.SpringBase.models.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class LoginResponse {
    private final String accessToken;
    private final Long id;
    private final String email;
    private final List<Role> roles;
    private final String name;
    private final String surname;
    private final String message;
}

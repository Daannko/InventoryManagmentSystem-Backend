package com.example.SpringBase.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;


@Data
@AllArgsConstructor
public class RegisterRequest {
    @Email
    private final String email;
    private final String password;
    private final String name;
    private final String surname;
}
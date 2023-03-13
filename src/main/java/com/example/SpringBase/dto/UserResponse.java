package com.example.SpringBase.dto;

import com.example.SpringBase.models.Role;
import lombok.Data;

import java.util.List;

@Data
public class UserResponse {
    private long id;
    private String email;
    private List<Role> roles;
    private String name;
    private String surname;
    private boolean verified;
}

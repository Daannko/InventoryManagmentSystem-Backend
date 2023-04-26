package com.example.InventoryManagmentSystem.models;

import com.example.InventoryManagmentSystem.dto.CompanyResponse;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    private String name;
    @OneToMany(mappedBy = "company",cascade = CascadeType.ALL)
    @JsonBackReference
    private List<User> employees;

    public Company(String name, List<User> employees) {
        this.name = name;
        this.employees = employees;
    }

    public Company() {}

    public CompanyResponse dto(){
        return new CompanyResponse(
                this.id,
                this.name,
                this.employees.stream().map(User::dto).collect(Collectors.toList()),
                "All companies"
        );
    }
}

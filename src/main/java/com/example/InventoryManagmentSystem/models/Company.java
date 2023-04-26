package com.example.InventoryManagmentSystem.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(nullable = false)
    private String name;
    @OneToMany(mappedBy = "company",cascade = CascadeType.ALL)
    @JsonBackReference
    private List<User> employees;

    public Company(String name, List<User> employees) {
        this.name = name;
        this.employees = employees;
    }

    public Company() {

    }
}

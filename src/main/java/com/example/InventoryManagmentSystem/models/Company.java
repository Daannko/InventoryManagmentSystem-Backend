package com.example.InventoryManagmentSystem.models;

import com.example.InventoryManagmentSystem.dto.CompanyResponse;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "company_id", nullable = false)
    private Long id;
    private String name;
    @OneToMany(mappedBy = "company",cascade = CascadeType.ALL)
    @JsonBackReference
    private List<User> employees;

    @ManyToMany(cascade = CascadeType.ALL)
    @JsonManagedReference
    @JoinTable(
            name = "storehouses_companies",
            joinColumns = @JoinColumn(name = "company_id"),
            inverseJoinColumns = @JoinColumn(name = "storehouse_id"))
    private List<Storehouse> storehouses;

    @JsonIgnore
    @ElementCollection
    @CollectionTable(name = "company_admins", joinColumns = @JoinColumn(name = "company_id"))
    @Column(name = "admin_email", nullable = false)
    private List<String> admins;

    public Company(String name,List<User> employees,String admin) {
        this.name = name;
        this.employees = employees;
        this.admins =  Collections.singletonList(admin);
    }

    public Company() {}

    public CompanyResponse dto(){
        return new CompanyResponse(
                this.id,
                this.name,
                this.employees.stream().map(User::dto).collect(Collectors.toList()),
                this.getAdmins(),
                this.storehouses,
                ""
        );
    }
}

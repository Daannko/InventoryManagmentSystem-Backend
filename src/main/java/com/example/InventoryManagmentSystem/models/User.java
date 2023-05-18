package com.example.InventoryManagmentSystem.models;

import com.example.InventoryManagmentSystem.dto.UserResponse;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;


@Entity(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<Role> roles;

    private String name;

    private String surname;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "managed_storehouses",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "storehouse_id"))
    List<Storehouse> managedStorehouses;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "company_id")
    @JsonManagedReference
    private Company company;
    private boolean verified;

    public UserResponse dto(){
        return new UserResponse(
                this.id,
                this.email,
                this.roles
                ,this.name,
                this.surname
        );
    }

    public boolean isAdmin(){
        return this.roles.contains(Role.ROLE_ADMIN);
    }


}

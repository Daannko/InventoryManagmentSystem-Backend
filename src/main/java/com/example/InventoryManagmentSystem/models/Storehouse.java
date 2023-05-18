package com.example.InventoryManagmentSystem.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@Entity
@Data
@Table(name = "storehouses")
public class Storehouse {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
    private String name;
    private String description;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "storehouse_id", referencedColumnName = "id")
    @JsonManagedReference
    private Localization localization;
    @ManyToMany(mappedBy = "managedStorehouses",cascade = CascadeType.ALL)
    @JsonIgnore
    List<User> owners;

    @ManyToMany(mappedBy = "storehouses",cascade = CascadeType.ALL)
    @JsonBackReference
    List<Company> companies;

    private boolean isBig;

}

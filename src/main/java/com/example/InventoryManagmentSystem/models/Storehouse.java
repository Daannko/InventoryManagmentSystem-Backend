package com.example.InventoryManagmentSystem.models;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@Entity
@Data
@Table(name = "storehouses")
public class Storehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;
    private Long localizationId;
    @ManyToMany(mappedBy = "managedStorehouses")
    List<User> owners;
    private String category;
    private boolean isBig;

}

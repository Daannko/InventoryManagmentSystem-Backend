package com.example.InventoryManagmentSystem.models;

import lombok.Data;

import javax.persistence.*;
import java.util.Map;

@Entity
@Data
@Table(name = "storehouses")
public class Storehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private String name;
    @Column
    private String description;

    @ElementCollection
    @CollectionTable(name = "")
    private Map<Integer,Integer> inventory;
    @Column
    private Long localizationId;
    @Column
    private String category;
    @Column
    private boolean isBig;

}

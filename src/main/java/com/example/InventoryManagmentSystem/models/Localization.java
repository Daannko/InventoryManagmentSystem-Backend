package com.example.InventoryManagmentSystem.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "localizations")
public class Localization {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    private double lat,lng;
    @Column(nullable = false)
    private String name;
    private String description;
    private String postCode;
    private String street;
    private String buildingNumber;
    private String country;
    @OneToOne(mappedBy = "localization")
    @JsonBackReference
    private Storehouse storehouse;

}


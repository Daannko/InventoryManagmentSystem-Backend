package com.example.InventoryManagmentSystem.models;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "localizations")
public class Localization {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long Id;
    private double lat,lng;
    @Column(nullable = false)
    private String name;
    private String description;
    private String postCode;
    private String street;
    private String buildingNumber;
    private String country;

}


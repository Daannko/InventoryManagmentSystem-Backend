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
    @Column
    private double lat,lng;
    @Column(nullable = false)
    private String name;
    @Column
    private String description;
    @Column
    private String postCode;
    @Column
    private String street;
    @Column
    private String buildingNumber;
    @Column
    private String country;

}


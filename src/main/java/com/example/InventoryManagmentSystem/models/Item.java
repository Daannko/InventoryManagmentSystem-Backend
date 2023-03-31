package com.example.InventoryManagmentSystem.models;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private String name;
    @Column
    private String description;
    @Column
    private double price;
    @Column
    private String category;
    @Column
    private boolean isAvailable;
    @Column
    private String manufacturer;



}

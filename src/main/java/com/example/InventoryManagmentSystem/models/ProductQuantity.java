package com.example.InventoryManagmentSystem.models;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
class ProductQuantity {

    @EmbeddedId
    ProductQuantityKey id;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    Product product;

    @ManyToOne
    @MapsId("storehouseId")
    @JoinColumn(name = "storehouse_id")
    Storehouse storehouse;

    int quantity;

}

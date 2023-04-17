package com.example.InventoryManagmentSystem.models;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
class ProductQuantityKey implements Serializable {

    @Column(name = "product_id")
    Long productId;

    @Column(name = "cstorehouse_id")
    Long storehouseId;

    // standard constructors, getters, and setters

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
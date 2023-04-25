package com.example.InventoryManagmentSystem.models;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "orders")
public class Order {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;
    private Long userId;
    private Long storehouseId;
    @OneToMany(mappedBy = "order")
    private List<Item> items;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
}

enum OrderStatus{
    AWAIT,PROCESSED,SHIPPED,DELIVERED,CANCELED
}

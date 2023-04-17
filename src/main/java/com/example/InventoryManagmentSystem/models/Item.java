package com.example.InventoryManagmentSystem.models;

import lombok.Data;
import org.aspectj.weaver.ast.Or;

import javax.persistence.*;

@Entity
@Data
@Table(name = "items")
public class Item {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    private Long productId;
    private Long quantity;

}

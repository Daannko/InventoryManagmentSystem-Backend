package com.example.InventoryManagmentSystem.models;

import com.example.InventoryManagmentSystem.dto.ItemResponse;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import org.aspectj.weaver.ast.Or;

import javax.persistence.*;

@Entity
@Data
@Table(name = "items")
public class Item {
    @Id
    @Column(name = "productId", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    @JsonBackReference
    private Order order;
    private Long productId;
    private int quantity;


}

package com.example.InventoryManagmentSystem.models;

import com.example.InventoryManagmentSystem.dto.OrderResponse;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import net.bytebuddy.dynamic.loading.InjectionClassLoader;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "orders")
@Builder
@AllArgsConstructor
public class Order {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private Long userId;
    private Long fromStorehouseId;
    private Long toStorehouseId;
    private Date createdAt;
    private Date statusChangeAt;
    @OneToMany(mappedBy = "order")
    @JsonManagedReference
    private List<Item> items;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    private String message;

    public Order() {

    }

    public OrderResponse responseDTO() {
        return OrderResponse.builder()
                .orderId(id)
                .formStorehouseId(this.fromStorehouseId)
                .toStorehouseId(this.toStorehouseId)
                .status(this.orderStatus)
                .createdAt(createdAt)
                .statusChangedAt(statusChangeAt)
                .message(this.message).build();
    }
}


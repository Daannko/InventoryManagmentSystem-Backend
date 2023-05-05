package com.example.InventoryManagmentSystem.models;

import com.example.InventoryManagmentSystem.dto.OrderResponse;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import net.bytebuddy.dynamic.loading.InjectionClassLoader;

import javax.persistence.*;
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
    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL)
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
                .userId(this.userId)
                .formStorehouseId(this.fromStorehouseId)
                .toStorehouseId(this.toStorehouseId)
                .status(this.orderStatus)
                .message(this.message).build();
    }
}


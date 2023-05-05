package com.example.InventoryManagmentSystem.controlers;

import com.example.InventoryManagmentSystem.dto.*;
import com.example.InventoryManagmentSystem.models.Order;
import com.example.InventoryManagmentSystem.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;


    @PostMapping("")
    public OrderResponse order(@RequestBody OrderRequest request){
        return orderService.add(request);
    }

    @GetMapping
    public List<Order> getAll(){
        return orderService.getAll();
    }
    @GetMapping("/{id}")
    public OrderResponse getAll(@PathVariable Long id){
        return orderService.getById(id);
    }

    @PostMapping("/process")
    public MessageResponse order(@RequestBody OrderProcessRequest request){
        return orderService.process(request);
    }

    @GetMapping("/user")
    public OrderForUserResponse getForUser(@RequestBody OrderForUserRequest request){
        return orderService.getForUser(request);
    }

}

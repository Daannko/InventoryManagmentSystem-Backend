package com.example.InventoryManagmentSystem.services;

import com.example.InventoryManagmentSystem.dto.*;
import com.example.InventoryManagmentSystem.models.*;
import com.example.InventoryManagmentSystem.repositories.*;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final StorehouseRepository storehouseRepository;
    private final UserRepository userRepository;
    private final ProductService productService;
    private final ProductRepository productRepository;
    private final UserService userService;
    private final QuantityRepository quantityRepository;
    private final ItemRepository itemRepository;


    public OrderResponse add(OrderRequest orderRequest) {
        Optional<Storehouse> fromStorehouseOptional = storehouseRepository.findById(orderRequest.getFormStorehouseId());
        Optional<Storehouse> toStorehouseOptional = storehouseRepository.findById(orderRequest.getToStorehouseId());
        Optional<User> userOptional = userRepository.findById(orderRequest.getUserId());

        if (fromStorehouseOptional.isEmpty() || toStorehouseOptional.isEmpty() || userOptional.isEmpty()) {
            return OrderResponse.builder().message("Can't find user or storehouses").build();
        }
        if (!fromStorehouseOptional.get().isBig()) {
            return OrderResponse.builder().message("This storehouse don't sell things (Debug this is not a big storehouse :( )").build();
        }

        if(!userService.getContextUser().getManagedStorehouses().contains(toStorehouseOptional.get())) {
            return OrderResponse.builder().message("You cant put an order on storehouse you dont manage").build();
        }

        Order order = Order.builder()
                .orderStatus(OrderStatus.AWAIT)
                .fromStorehouseId(orderRequest.getFormStorehouseId())
                .toStorehouseId(orderRequest.getToStorehouseId())
                .userId(orderRequest.getUserId())
                .build();

        for (Item i: orderRequest.getItems())
            i.setOrder(order);
        order.setItems(orderRequest.getItems());

        orderRepository.save(order);
        itemRepository.saveAll(orderRequest.getItems());
        return mapToOrderResponse(order);
    }

    public MessageResponse process(OrderProcessRequest orderProcessRequest) {

        Optional<Order> optionalOrder = orderRepository.findById(orderProcessRequest.getOrderId());

        if (optionalOrder.isEmpty()) {
            return new MessageResponse("There is no order with this ID");
        }

        Order order = optionalOrder.get();
        User user = userService.getContextUser();
        if (user.getManagedStorehouses().stream().noneMatch(e -> e.getId().equals(order.getFromStorehouseId()))) {
            return new MessageResponse("You cant manage that store");
        }

        if(order.getOrderStatus().equals(orderProcessRequest.getOrderStatus())){
            return new MessageResponse("Order was already " + orderProcessRequest.getOrderStatus().name().toLowerCase());
        }

        if(order.getOrderStatus().equals(OrderStatus.CANCELED)){
            return new MessageResponse("You cant change status on canceled order");
        }

        if(orderProcessRequest.getOrderStatus().equals(OrderStatus.CANCELED) &&
                (order.getOrderStatus().equals(OrderStatus.SHIPPED) || order.getOrderStatus().equals(OrderStatus.DELIVERED)))
        {
            if(order.getOrderStatus().equals(OrderStatus.DELIVERED)){
                return new MessageResponse("You cant cancel delivered order");
            }
            MessageResponse status = manageItems(order,order.getFromStorehouseId(),1);
            if(status.getMessage().contains("Order status changed successfully")){
                order.setOrderStatus(orderProcessRequest.getOrderStatus());
                orderRepository.save(order);
            }
            return status;
        }

        if(orderProcessRequest.getOrderStatus().equals(OrderStatus.ACCEPTED) || orderProcessRequest.getOrderStatus().equals(OrderStatus.REJECTED)){
            if(!order.getOrderStatus().equals(OrderStatus.AWAIT))
                return new MessageResponse("You cant do" + orderProcessRequest.getOrderStatus().name()
                        + " on object that was already " + order.getOrderStatus().name());
        }

        if(orderProcessRequest.getOrderStatus().equals(OrderStatus.SHIPPED)){
            if(!order.getOrderStatus().equals(OrderStatus.ACCEPTED)){
                return new MessageResponse("You cant ship an order that was not accepted");
            }
            MessageResponse status = manageItems(order,order.getFromStorehouseId(),-1);
            if(status.getMessage().contains("Order status changed successfully")){
                order.setOrderStatus(orderProcessRequest.getOrderStatus());
                orderRepository.save(order);
            }
            return status;
        }

        if(orderProcessRequest.getOrderStatus().equals(OrderStatus.DELIVERED)){
            if(!order.getOrderStatus().equals(OrderStatus.SHIPPED)){
                return new MessageResponse("You cant deliver an order that was not shipped");
            }
            MessageResponse status = manageItems(order,order.getToStorehouseId(),1);
            if(status.getMessage().contains("Order status changed successfully")){
                order.setOrderStatus(orderProcessRequest.getOrderStatus());
                orderRepository.save(order);
            }
            return status;
        }

        order.setOrderStatus(orderProcessRequest.getOrderStatus());
        orderRepository.save(order);
        return new MessageResponse("Order status changed successfully:)");
    }

    private MessageResponse manageItems(Order order,Long storehouseId, int multiplier) {

        if (multiplier < 0 && !productService.checkProductsQuantity(order.getFromStorehouseId(), order.getItems())) {
            return new MessageResponse("Not enough items in storehouse");
        }
        for (int i = 0; i < order.getItems().size(); i++) {
            ProductResponse productResponse = productService.changeProductQuantity(ProductQuantityRequest.builder()
                    .productId(order.getItems().get(i).getProductId())
                    .storehouseId(storehouseId)
                    .quantity(multiplier * order.getItems().get(i).getQuantity())
                    .build());

            if (!productResponse.getMessage().contains("Operation succeeded")) {
                return new MessageResponse(productResponse.getMessage());
            }
        }
        return new MessageResponse("Order status changed successfully");
    }

    public List<Order> getAll(){
        return orderRepository.findAll();
    }

    public OrderResponse getById(Long id){
        Optional<Order> optionalOrder = orderRepository.findById(id);
        assert optionalOrder.orElse(null) != null;
        return mapToOrderResponse(optionalOrder.orElse(null));

    }

    public OrderForUserResponse getForUser(OrderForUserRequest request){


        User user = userRepository.getById(request.getUserId());

        OrderForUserResponse response = new OrderForUserResponse();
        response.setOrderedToYourStorehouses(new ArrayList<>());
        response.setOrderedFromYourStorehouses(new ArrayList<>());

        for(int i = 0 ; i < user.getManagedStorehouses().size() ; i++){
            for(Order order : orderRepository.getByFromStorehouseId(user.getManagedStorehouses().get(i).getId())){
                if(request.getOrderStatus() != null && !order.getOrderStatus().equals(request.getOrderStatus())) continue;
                response.getOrderedFromYourStorehouses().add(mapToOrderResponse(order));
            }
            for(Order order : orderRepository.getByToStorehouseId(user.getManagedStorehouses().get(i).getId())){
                if(request.getOrderStatus() != null && !order.getOrderStatus().equals(request.getOrderStatus())) continue;
                response.getOrderedToYourStorehouses().add(mapToOrderResponse(order));
            }
        }

        return response;
    }

    private OrderResponse mapToOrderResponse(Order order){
        OrderResponse orderResponse = order.responseDTO();
        orderResponse.setItems(order.getItems().stream().map(e -> new ItemResponse(productRepository.getById(e.getProductId()).dto(),e.getQuantity())).collect(Collectors.toList()));
        return orderResponse;
    }


}


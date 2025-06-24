package com.adi.order_service.controller;

import com.adi.order_service.model.Order;
import com.adi.order_service.model.OrderRequest;
import com.adi.order_service.model.OrderResponse;
import com.adi.order_service.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//POST /orders — create order
//GET /orders/user — fetch current user’s orders
//GET /orders/{id} — fetch single order
//GET /orders/all — admin-only

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService service;

    @PostMapping("")
    public ResponseEntity<String> placeOrder(
            @RequestHeader("X-User-Id") int userId,
            @RequestBody OrderRequest request
    ) {
        service.placeOrder(userId, request);
        return new ResponseEntity<>("Order placed", HttpStatus.CREATED);
    }

    @GetMapping("/user")
    public ResponseEntity<List<OrderResponse>> getOrdersForUser(
            @RequestHeader("X-User-Id") int userId
    ) {
        List<OrderResponse> orders = service.getOrdersForUser(userId);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable int orderId) {
        return new ResponseEntity<>(service.getOrder(orderId), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return new ResponseEntity<>(service.getAllOrders(), HttpStatus.OK);
    }
}

package com.adi.order_service.service;

import com.adi.order_service.model.*;
import com.adi.order_service.repo.OrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepo repo;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private EmailService emailService;

    public void placeOrder(int userId, OrderRequest request) {
        Order newOrder = new Order();
        newOrder.setUserId(userId);
        newOrder.setStatus("PLACED");

        List<OrderItem> items = new ArrayList<>();

        for(OrderItemRequest req : request.getItems()) {
            OrderItem item = new OrderItem();
            item.setProductId(req.getProductId());
            item.setQuantity(req.getQuantity());
            item.setOrder(newOrder);
            items.add(item);
        }

        newOrder.setItems(items);
        repo.save(newOrder);

        // For updating the products table

        for (OrderItemRequest item : request.getItems()) {
            ReduceInventory reduceInventory = new ReduceInventory();
            reduceInventory.setProductId(item.getProductId());
            reduceInventory.setQuantity(item.getQuantity());

            webClientBuilder.build()
                    .post()
                    .uri("http://INVENTORY-SERVICE/inventory/reduce")
                    .bodyValue(reduceInventory)
                    .retrieve()
                    .toBodilessEntity()
                    .block();

            // Fetching updated quantity
            Integer newQuantity = webClientBuilder.build()
                    .get()
                    .uri("http://INVENTORY-SERVICE/inventory/" + item.getProductId())
                    .retrieve()
                    .bodyToMono(Integer.class)
                    .block();

            // update product status
            boolean isActive = newQuantity > 0;

            webClientBuilder.build()
                    .put()
                    .uri(uriBuilder -> uriBuilder
                            .scheme("http")
                            .host("PRODUCT-SERVICE")
                            .path("/products/update-active")
                            .queryParam("productId", item.getProductId())
                            .queryParam("isActive", isActive)
                            .build())
                    .retrieve()
                    .toBodilessEntity()
                    .block();
        }

        // Sending confirmation email

        String email = webClientBuilder
                .build()
                .get()
                .uri("http://USER-SERVICE/users/email/" + userId)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        String emailContent = "Hi!, Your order with Id " + newOrder.getId() + "has been placed!";
        emailService.sendConfirmationMail(email, emailContent);
    }

    public List<OrderResponse> getOrdersForUser(int userId) {
        List<Order> orders = repo.findByUserId(userId);
        return getProperResponse(orders);
    }

    public OrderResponse getOrder(int orderId) {
        Order order = repo.findById(orderId).get();
        return getProperResponse(List.of(order)).get(0);
    }

    public List<OrderResponse> getAllOrders() {
        List<Order> orders = repo.findAll();
        return getProperResponse(orders);
    }

    public List<OrderResponse> getProperResponse(List<Order> orders) {
        return orders.stream().map(
                order -> {
                    List<OrderItemResponse> items = order.getItems().stream().map(item -> {
                        OrderItemResponse res = new OrderItemResponse();
                        res.setProductId(item.getProductId());
                        res.setQuantity(item.getQuantity());
                        return res;
                    }).toList();

                    OrderResponse response = new OrderResponse();
                    response.setOrderId(order.getId());
                    response.setUserId(order.getUserId());
                    response.setOrderDate(order.getOrderDate());
                    response.setOrderTime(order.getOrderTime());
                    response.setStatus(order.getStatus());
                    response.setItems(items);
                    return response;
                }).toList();
    }
}

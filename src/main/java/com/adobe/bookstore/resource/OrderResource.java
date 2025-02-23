package com.adobe.bookstore.resource;

import com.adobe.bookstore.model.Order;
import com.adobe.bookstore.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders/")
public class OrderResource {
    @Autowired
    private OrderService orderService;

    public OrderResource(OrderService orderService){
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody Order order) {
        try {
            Order savedOrder = orderService.processOrder(order);
            return ResponseEntity.ok(savedOrder.getId());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Order>> getOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }
}

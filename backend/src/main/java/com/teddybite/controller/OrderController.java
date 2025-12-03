package com.teddybite.controller;

import com.teddybite.entity.Order;
import com.teddybite.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        // Set server-side fields if missing
        if (order.getCreatedAt() == null) {
            order.setCreatedAt(LocalDateTime.now().toString());
        }
        
        // Generate IDs if missing (for demo purposes)
        if (order.getPayment() != null && order.getPayment().getTransactionId() == null) {
            order.getPayment().setTransactionId(UUID.randomUUID().toString().substring(0, 8));
        }
        
        Order savedOrder = orderRepository.save(order);
        return ResponseEntity.ok(savedOrder);
    }
    
    @GetMapping
    public ResponseEntity<Iterable<Order>> getAllOrders() {
        return ResponseEntity.ok(orderRepository.findAll());
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Backend is running");
    }
}

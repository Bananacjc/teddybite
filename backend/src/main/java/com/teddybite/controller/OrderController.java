package com.teddybite.controller;

import com.teddybite.dto.OrderDTO;
import com.teddybite.entity.Order;
import com.teddybite.service.interfaceService.IOrderService;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final IOrderService orderService;

    public OrderController(IOrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@Valid @RequestBody OrderDTO orderDTO) {
        
        Order savedOrder = orderService.createOrder(orderDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedOrder.getOrderId())
                .toUri();

        return ResponseEntity.created(location).body(savedOrder);
    }
    
    @GetMapping
    public List<Order> findAll() {
        return orderService.readOrderAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> findById(@PathVariable String id) {
        return ResponseEntity.of(orderService.readOrderById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable String id, @Valid @RequestBody OrderDTO updateDTO) {
        return orderService.readOrderById(id)
                .map(currentOrder -> {
                    currentOrder.setOrderItems(updateDTO.getOrderItems());
                    currentOrder.setPaymentId(updateDTO.getPaymentId());

                    Order updateOrder = orderService.updateOrder(currentOrder);

                    return ResponseEntity.ok(updateOrder);
                })
                .orElse(ResponseEntity.notFound().build());
                
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        if (orderService.readOrderById(id).isPresent()) {
            orderService.deleteOrderById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Backend is running");
    }
}

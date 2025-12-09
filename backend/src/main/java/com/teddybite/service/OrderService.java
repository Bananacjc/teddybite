package com.teddybite.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.teddybite.dto.OrderDTO;
import com.teddybite.entity.Order;
import com.teddybite.repository.OrderRepository;

@Service
public class OrderService implements IOrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order createOrder(OrderDTO orderDTO) {
        Order newOrder = new Order();

        newOrder.setOrderItems(orderDTO.getOrderItems());
        newOrder.setPaymentId(orderDTO.getPaymentId());
        newOrder.setCreatedAt(LocalDateTime.now());

        return orderRepository.save(newOrder);
    }

    @Override
    public List<Order> readOrderAll() {
        return orderRepository.findAll();
    }

    @Override
    public Optional<Order> readOrderById(String id) {
        return orderRepository.findById(id);
    }

    @Override
    public Order updateOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public void deleteOrderById(String id) {
        orderRepository.deleteById(id);
    }   

}
package com.teddybite.service;

import java.util.List;
import java.util.Optional;

import com.teddybite.dto.OrderDTO;
import com.teddybite.entity.Order;

public interface IOrderService {
    
    public Order createOrder(OrderDTO orderDTO);
    public List<Order> readOrderAll();
    public Optional<Order> readOrderById(String id);
    public Order updateOrder(Order order);
    public void deleteOrderById(String id);
}

package com.teddybite.service;
import java.util.List;
import com.teddybite.dto.OrderRequest;
import com.teddybite.model.Order;

public interface OrderService {
    
    Order createOrder(List<OrderRequest> items); 

    Order getOrderById(String orderId);

    boolean cancelOrder(String orderId);
}

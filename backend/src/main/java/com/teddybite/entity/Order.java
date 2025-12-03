package com.teddybite.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Data
@Document(collection = "orders")
public class Order {
    @Id
    private String id;
    private List<OrderItem> orderItems;
    private double subTotal;
    private double tax;
    private double totalAmount;
    private Payment payment;
    private String status;
    private String createdAt;
}

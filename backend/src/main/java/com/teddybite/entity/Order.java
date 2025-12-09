package com.teddybite.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;
import java.time.LocalDateTime;

@Data
@Document(collection = "orders")
public class Order {
    @Id
    private String orderId;
    private List<OrderItem> orderItems;
    private String paymentId;
    private LocalDateTime createdAt;
}

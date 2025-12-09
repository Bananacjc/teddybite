package com.teddybite.dto;

import java.util.List;

import com.teddybite.entity.OrderItem;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class OrderDTO {
    
    @NotEmpty(message = "Order items is required")
    private List<OrderItem> orderItems;

    @NotBlank(message = "Payment ID is required")
    private String paymentId;

}

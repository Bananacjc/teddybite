package com.teddybite.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class PaymentDTO {
    
    @NotNull(message = "Payment Amount is required")
    @Min(value = 0, message = "Payment Amount cannot be 0")
    private double paymentAmount;
}

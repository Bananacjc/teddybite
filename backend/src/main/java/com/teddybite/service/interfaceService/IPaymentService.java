package com.teddybite.service.interfaceService;

import java.util.List;
import java.util.Optional;

import com.teddybite.dto.PaymentDTO;
import com.teddybite.entity.Payment;

public interface IPaymentService {
    
    public Payment createPayment(PaymentDTO paymentDTO);
    public List<Payment> readPaymentAll();
    public Optional<Payment> readPaymentById(String id);
    public Payment updatePayment(Payment payment);
    public void deletePaymentById(String id);
    
    
}

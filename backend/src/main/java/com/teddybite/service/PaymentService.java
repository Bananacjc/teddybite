package com.teddybite.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import com.teddybite.dto.PaymentDTO;
import com.teddybite.entity.Payment;
import com.teddybite.repository.PaymentRepository;
import com.teddybite.service.interfaceService.IPaymentService;

@Service
public class PaymentService implements IPaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Payment createPayment(PaymentDTO paymentDTO) {
        Payment newPayment = new Payment();
        newPayment.setPaymentAmount(paymentDTO.getPaymentAmount());
        return paymentRepository.save(newPayment);
    }

    @Override
    public List<Payment> readPaymentAll() {
        return paymentRepository.findAll();
    }

    @Override
    public Optional<Payment> readPaymentById(String id) {
        return paymentRepository.findById(id);
    }

    @Override
    public Payment updatePayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    @Override
    public void deletePaymentById(String id) {
        paymentRepository.deleteById(id);
    }

}

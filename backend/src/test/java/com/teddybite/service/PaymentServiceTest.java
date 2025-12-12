package com.teddybite.service;

import com.teddybite.dto.PaymentDTO;
import com.teddybite.entity.Payment;
import com.teddybite.entity.types.PaymentType;
import com.teddybite.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentService paymentService;

    private PaymentDTO paymentDTO;
    private Payment payment;

    @BeforeEach
    void setUp() {
        paymentDTO = new PaymentDTO();
        paymentDTO.setPaymentAmount(50.0);
        paymentDTO.setPaymentType(PaymentType.CREDIT_CARD);

        payment = new Payment();
        payment.setPaymentId("pay-123");
        payment.setPaymentAmount(50.0);
        payment.setPaymentType(PaymentType.CREDIT_CARD);
    }

    @Test
    void testCreatePayment() {
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> {
            Payment saved = invocation.getArgument(0);
            saved.setPaymentId("pay-123"); 
            return saved;
        });

        Payment result = paymentService.createPayment(paymentDTO);

        assertNotNull(result);
        assertEquals("pay-123", result.getPaymentId());
        assertEquals(50.0, result.getPaymentAmount());
        assertEquals(PaymentType.CREDIT_CARD, result.getPaymentType());
        
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testReadPaymentAll() {
        when(paymentRepository.findAll()).thenReturn(List.of(payment));

        List<Payment> result = paymentService.readPaymentAll();

        assertEquals(1, result.size());
        verify(paymentRepository, times(1)).findAll();
    }

    @Test
    void testReadPaymentById_Found() {
        when(paymentRepository.findById("pay-123")).thenReturn(Optional.of(payment));

        Optional<Payment> result = paymentService.readPaymentById("pay-123");

        assertTrue(result.isPresent());
        assertEquals("pay-123", result.get().getPaymentId());
    }

    @Test
    void testReadPaymentById_NotFound() {
        when(paymentRepository.findById("unknown")).thenReturn(Optional.empty());

        Optional<Payment> result = paymentService.readPaymentById("unknown");

        assertFalse(result.isPresent());
    }

    @Test
    void testUpdatePayment() {
        when(paymentRepository.save(payment)).thenReturn(payment);

        Payment result = paymentService.updatePayment(payment);

        assertNotNull(result);
        assertEquals("pay-123", result.getPaymentId());
        verify(paymentRepository, times(1)).save(payment);
    }

    @Test
    void testDeletePaymentById() {
        doNothing().when(paymentRepository).deleteById("pay-123");

        paymentService.deletePaymentById("pay-123");

        verify(paymentRepository, times(1)).deleteById("pay-123");
    }
}
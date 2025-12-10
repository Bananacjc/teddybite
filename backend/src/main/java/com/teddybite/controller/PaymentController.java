package com.teddybite.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.teddybite.dto.PaymentDTO;
import com.teddybite.entity.Payment;
import com.teddybite.service.interfaceService.IPaymentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    
    private final IPaymentService paymentService;

    public PaymentController(IPaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<Payment> save(@Valid @RequestBody PaymentDTO payment) {
        
        Payment savedPayment = paymentService.createPayment(payment);

        URI location = ServletUriComponentsBuilder 
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedPayment.getPaymentAmount())
                .toUri();

        return ResponseEntity.created(location).body(savedPayment);
    }

    @GetMapping
    public List<Payment> findAll() {
        return paymentService.readPaymentAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> findById(@PathVariable String id) {
        return ResponseEntity.of(paymentService.readPaymentById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Payment> updatePayment(@PathVariable String id, 
            @Valid @RequestBody PaymentDTO updateDTO) {
        return paymentService.readPaymentById(id)
                .map(currentPayment -> {
                    currentPayment.setPaymentAmount(updateDTO.getPaymentAmount());
                    
                    Payment updatedPayment = paymentService.updatePayment(currentPayment);

                    return ResponseEntity.ok(updatedPayment);
                })
                .orElse(ResponseEntity.notFound().build());

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        if (paymentService.readPaymentById(id).isPresent()) {
            paymentService.deletePaymentById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
}

package com.teddybite.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teddybite.dto.PaymentDTO;
import com.teddybite.entity.Payment;
import com.teddybite.entity.types.PaymentType;
import com.teddybite.service.interfaceService.IPaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IPaymentService paymentService;

    @Autowired
    private ObjectMapper objectMapper;

    private Payment samplePayment;
    private PaymentDTO samplePaymentDTO;

    @BeforeEach
    void setUp() {
        samplePayment = new Payment();
        samplePayment.setPaymentId("pay-001");
        samplePayment.setPaymentAmount(100.0);
        samplePayment.setPaymentType(PaymentType.CASH);

        samplePaymentDTO = new PaymentDTO();
        samplePaymentDTO.setPaymentAmount(100.0);
        samplePaymentDTO.setPaymentType(PaymentType.CASH);
    }

    @Test
    public void testFindAll() throws Exception {
        when(paymentService.readPaymentAll()).thenReturn(List.of(samplePayment));

        mockMvc.perform(get("/api/payments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].paymentId").value("pay-001"));
    }

    @Test
    public void testFindById_Success() throws Exception {
        when(paymentService.readPaymentById("pay-001")).thenReturn(Optional.of(samplePayment));

        mockMvc.perform(get("/api/payments/pay-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paymentId").value("pay-001"));
    }

    @Test
    public void testFindById_NotFound() throws Exception {
        when(paymentService.readPaymentById("unknown")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/payments/unknown"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreatePayment() throws Exception {
        when(paymentService.createPayment(any(PaymentDTO.class))).thenReturn(samplePayment);

        mockMvc.perform(post("/api/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(samplePaymentDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.paymentId").value("pay-001"));
    }

    @Test
    public void testUpdatePayment_Success() throws Exception {
        when(paymentService.readPaymentById("pay-001")).thenReturn(Optional.of(samplePayment));
        when(paymentService.updatePayment(any(Payment.class))).thenReturn(samplePayment);

        mockMvc.perform(put("/api/payments/pay-001")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(samplePaymentDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paymentId").value("pay-001"));
    }

    @Test
    public void testUpdatePayment_NotFound() throws Exception {
        when(paymentService.readPaymentById("unknown")).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/payments/unknown")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(samplePaymentDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteById_Success() throws Exception {
        when(paymentService.readPaymentById("pay-001")).thenReturn(Optional.of(samplePayment));
        doNothing().when(paymentService).deletePaymentById("pay-001");

        mockMvc.perform(delete("/api/payments/pay-001"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteById_NotFound() throws Exception {
        when(paymentService.readPaymentById("unknown")).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/payments/unknown"))
                .andExpect(status().isNotFound());
    }
}
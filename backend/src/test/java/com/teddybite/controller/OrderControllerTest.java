package com.teddybite.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teddybite.dto.OrderDTO;
import com.teddybite.entity.Order;
import com.teddybite.entity.OrderItem;
import com.teddybite.service.interfaceService.IOrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IOrderService orderService;

    @Autowired
    private ObjectMapper objectMapper; 
    private Order sampleOrder;
    private OrderDTO sampleOrderDTO;

    @BeforeEach
    void setUp() {
        OrderItem item = new OrderItem();
        item.setItemId("item-001");
        item.setItemName("Beef Burger");
        item.setUnitPrice(15.0);
        item.setQuantity(2);
        item.setLineTotal(30.0);
        item.setRemarks(new ArrayList<>()); 

        sampleOrder = new Order();
        sampleOrder.setOrderId("order-123");
        sampleOrder.setPaymentId("pay-999");
        sampleOrder.setCreatedAt(LocalDateTime.now());
        sampleOrder.setOrderItems(List.of(item));

        sampleOrderDTO = new OrderDTO();
        sampleOrderDTO.setPaymentId("pay-999");
        sampleOrderDTO.setOrderItems(List.of(item));
    }

    @Test
    public void testFindAll() throws Exception {
        when(orderService.readOrderAll()).thenReturn(List.of(sampleOrder));

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk()) // Expect HTTP 200 OK
                .andExpect(jsonPath("$", hasSize(1))) // Expect JSON array size is 1
                .andExpect(jsonPath("$[0].orderId").value("order-123")); // Verify ID matches
    }

    @Test
    public void testFindById_Success() throws Exception {
        when(orderService.readOrderById("order-123")).thenReturn(Optional.of(sampleOrder));

        mockMvc.perform(get("/api/orders/order-123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value("order-123"));
    }

    @Test
    public void testFindById_NotFound() throws Exception {
        when(orderService.readOrderById("unknown-id")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/orders/unknown-id"))
                .andExpect(status().isNotFound()); // Expect HTTP 404 Not Found
    }

    @Test
    public void testCreateOrder() throws Exception {
        // Mock behavior: Service returns the saved order
        when(orderService.createOrder(any(OrderDTO.class))).thenReturn(sampleOrder);

        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleOrderDTO))) // Convert DTO to JSON
                .andExpect(status().isCreated()) // Expect HTTP 201 Created
                .andExpect(header().exists("Location")) // Verify Location header exists
                .andExpect(jsonPath("$.orderId").value("order-123"));
    }

    @Test
    public void testUpdateOrder() throws Exception {
        when(orderService.readOrderById("order-123")).thenReturn(Optional.of(sampleOrder));
        when(orderService.updateOrder(any(Order.class))).thenReturn(sampleOrder);

        mockMvc.perform(put("/api/orders/order-123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleOrderDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value("order-123"));
    }

    @Test
    public void testDeleteById_Success() throws Exception {
        when(orderService.readOrderById("order-123")).thenReturn(Optional.of(sampleOrder));
        doNothing().when(orderService).deleteOrderById("order-123");

        mockMvc.perform(delete("/api/orders/order-123"))
                .andExpect(status().isNoContent()); // Expect HTTP 204 No Content
    }

    @Test
    public void testDeleteById_NotFound() throws Exception {
        when(orderService.readOrderById("unknown-id")).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/orders/unknown-id"))
                .andExpect(status().isNotFound()); // Expect HTTP 404
    }

    @Test
    public void testHealthCheck() throws Exception {
        mockMvc.perform(get("/api/orders/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("Backend is running"));
    }
}
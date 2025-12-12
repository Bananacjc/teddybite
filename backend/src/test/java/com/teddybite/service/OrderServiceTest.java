package com.teddybite.service;

import com.teddybite.dto.OrderDTO;
import com.teddybite.entity.Item;
import com.teddybite.entity.Order;
import com.teddybite.entity.OrderItem;
import com.teddybite.entity.types.ItemCategory;
import com.teddybite.entity.types.Remark;
import com.teddybite.repository.ItemRepository;
import com.teddybite.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private OrderService orderService;

    private Item dbItem;
    private OrderDTO orderDTO;
    private Order sampleOrder;

    @BeforeEach
    void setUp() {
        dbItem = new Item();
        dbItem.setItemId("item-001");
        dbItem.setItemName("Beef Burger");
        dbItem.setItemPrice(15.0);
        dbItem.setItemImage("burger.jpg");
        dbItem.setItemCategory(ItemCategory.BURGER);

        OrderItem incomingItem = new OrderItem();
        incomingItem.setItemId("item-001");
        incomingItem.setQuantity(2);
        incomingItem.setRemarks(new ArrayList<>()); 

        orderDTO = new OrderDTO();
        orderDTO.setPaymentId("pay-123");
        orderDTO.setOrderItems(List.of(incomingItem));

        sampleOrder = new Order();
        sampleOrder.setOrderId("order-123");
        sampleOrder.setPaymentId("pay-123");
        sampleOrder.setOrderItems(List.of(incomingItem));
    }

    @Test
    void testCreateOrder_Success() {
        when(itemRepository.findById("item-001")).thenReturn(Optional.of(dbItem));
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> {
            Order o = i.getArgument(0);
            o.setOrderId("new-id");
            return o;
        });

        Order result = orderService.createOrder(orderDTO);

        assertNotNull(result);
        assertEquals("new-id", result.getOrderId());
        assertEquals(30.0, result.getOrderItems().get(0).getLineTotal());
    }

    @Test
    void testCreateOrder_WithRemarks() {
        OrderItem itemWithRemark = new OrderItem();
        itemWithRemark.setItemId("item-001");
        itemWithRemark.setQuantity(1);
        itemWithRemark.setRemarks(List.of(Remark.EXTRA_CHEESE)); 

        OrderDTO remarkDTO = new OrderDTO();
        remarkDTO.setPaymentId("pay-rem");
        remarkDTO.setOrderItems(List.of(itemWithRemark));

        when(itemRepository.findById("item-001")).thenReturn(Optional.of(dbItem));
        when(orderRepository.save(any(Order.class))).thenReturn(new Order());

        assertDoesNotThrow(() -> orderService.createOrder(remarkDTO));
    }

    
    @Test
    void testCreateOrder_WithNullRemarks() {
        OrderItem itemNullRemark = new OrderItem();
        itemNullRemark.setItemId("item-001");
        itemNullRemark.setQuantity(1);
        itemNullRemark.setRemarks(null); 

        OrderDTO nullRemarkDTO = new OrderDTO();
        nullRemarkDTO.setPaymentId("pay-null");
        nullRemarkDTO.setOrderItems(List.of(itemNullRemark));

        when(itemRepository.findById("item-001")).thenReturn(Optional.of(dbItem));
        
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArgument(0));

        Order result = orderService.createOrder(nullRemarkDTO);
        
        assertNotNull(result);
        assertNotNull(result.getOrderItems().get(0).getRemarks()); 
    }

    
    @Test
    void testCreateOrder_ItemNotFound() {
        when(itemRepository.findById("item-001")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> orderService.createOrder(orderDTO));
    }

    @Test
    void testReadOrderAll() {
        orderService.readOrderAll();
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void testReadOrderById_Found() {
        when(orderRepository.findById("order-123")).thenReturn(Optional.of(sampleOrder));
        assertTrue(orderService.readOrderById("order-123").isPresent());
    }

    @Test
    void testReadOrderById_NotFound() {
        when(orderRepository.findById("unknown")).thenReturn(Optional.empty());
        assertFalse(orderService.readOrderById("unknown").isPresent());
    }

    @Test
    void testUpdateOrder() {
        when(orderRepository.save(sampleOrder)).thenReturn(sampleOrder);
        orderService.updateOrder(sampleOrder);
        verify(orderRepository, times(1)).save(sampleOrder);
    }

    @Test
    void testDeleteOrderById() {
        orderService.deleteOrderById("del-id");
        verify(orderRepository, times(1)).deleteById("del-id");
    }
}
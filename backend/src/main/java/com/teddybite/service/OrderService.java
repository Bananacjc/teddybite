package com.teddybite.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.teddybite.dto.OrderDTO;
import com.teddybite.entity.Item;
import com.teddybite.entity.Order;
import com.teddybite.entity.OrderItem;
import com.teddybite.entity.remark.IRemark;
import com.teddybite.entity.remark.RemarkFactory;
import com.teddybite.entity.types.Remark;
import com.teddybite.repository.ItemRepository;
import com.teddybite.repository.OrderRepository;
import com.teddybite.service.interfaceService.IOrderService;

@Service
public class OrderService implements IOrderService {

    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;

    public OrderService(OrderRepository orderRepository, ItemRepository itemRepository) {
        this.orderRepository = orderRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    public Order createOrder(OrderDTO orderDTO) {
        Order newOrder = new Order();

        List<OrderItem> processedItems = orderDTO.getOrderItems()
                .stream()
                .map(incomingItem -> {

                    Item dbItem = itemRepository.findById(incomingItem.getItemId())
                            .orElseThrow(() -> new RuntimeException("Item not found: " + incomingItem.getItemName()));
                    
                    IRemark remarkStrategy = RemarkFactory.getStrategy(dbItem.getItemCategory());
                    
                    List<Remark> requestedRemarks = incomingItem.getRemarks();
                    incomingItem.setRemarks(new ArrayList<>());

                    if (requestedRemarks != null) {
                        for (Remark remark : requestedRemarks) {
                            remarkStrategy.applyRemark(incomingItem, remark);
                        }
                    }

                    incomingItem.setItemName(dbItem.getItemName());
                    incomingItem.setUnitPrice(dbItem.getItemPrice());
                    incomingItem.setLineTotal(dbItem.getItemPrice() * incomingItem.getQuantity());

                    return incomingItem;
                }).toList();

        newOrder.setOrderItems(processedItems);
        newOrder.setPaymentId(orderDTO.getPaymentId());
        newOrder.setCreatedAt(LocalDateTime.now());

        return orderRepository.save(newOrder);
    }

    @Override
    public List<Order> readOrderAll() {
        return orderRepository.findAll();
    }

    @Override
    public Optional<Order> readOrderById(String id) {
        return orderRepository.findById(id);
    }

    @Override
    public Order updateOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public void deleteOrderById(String id) {
        orderRepository.deleteById(id);
    }

}
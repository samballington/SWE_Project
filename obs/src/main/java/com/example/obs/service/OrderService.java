package com.example.obs.service;

import com.example.obs.model.*;
import com.example.obs.repository.OrderRepository;
import com.example.obs.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;

    @Transactional
    public Order createOrder(User user, List<Map<String,Object>> cartItems,
                             BigDecimal subtotal, BigDecimal tax, BigDecimal total,
                             String address, String paymentInfo, String orderNumber){
        Order order = new Order();
        order.setUser(user);
        order.setOrderNumber(orderNumber);
        order.setSubtotal(subtotal);
        order.setTax(tax);
        order.setTotal(total);
        order.setShippingAddress(address);
        order.setPaymentInfo(paymentInfo);
        order.setEmail(user.getEmail());

        Order saved = orderRepository.save(order);

        // save items
        for(Map<String,Object> map: cartItems){
            Book book = (Book) map.get("book"); // will add book ref
            int qty = (int) map.get("quantity");
            BigDecimal price = (BigDecimal) map.get("price");
            OrderItem item = new OrderItem(saved, book, qty, price);
            orderItemRepository.save(item);
            saved.getItems().add(item);
        }
        return saved;
    }
    
    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findByUserOrderByCreatedAtDesc(user);
    }
    
    public Order getOrderByIdAndUser(Long orderId, User user) {
        return orderRepository.findByIdAndUser(orderId, user);
    }
    
    public List<Order> getAllOrders() {
        return orderRepository.findAllByOrderByCreatedAtDesc();
    }
} 
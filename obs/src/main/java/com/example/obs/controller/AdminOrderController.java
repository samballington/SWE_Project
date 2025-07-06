package com.example.obs.controller;

import com.example.obs.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/orders")
public class AdminOrderController {

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping
    public String listOrders(Model model){
        model.addAttribute("orders", orderRepository.findAll());
        return "admin/orders";
    }

    @GetMapping("/{id}")
    public String orderDetails(@PathVariable Long id, Model model){
        var orderOpt = orderRepository.findById(id);
        if(orderOpt.isEmpty()){
            return "redirect:/admin/orders";
        }
        model.addAttribute("order", orderOpt.get());
        return "admin/order-details";
    }
} 
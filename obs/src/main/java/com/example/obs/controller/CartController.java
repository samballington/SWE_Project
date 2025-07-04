package com.example.obs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;

@Controller
public class CartController {

    @GetMapping("/cart")
    public String cartPage(Model model) {
        List<Map<String, Object>> cartItems = new ArrayList<>();

        Map<String, Object> item1 = new HashMap<>();
        item1.put("title", "Little Prince");
        item1.put("price", 15.99);
        item1.put("quantity", 1);
        cartItems.add(item1);

        Map<String, Object> item2 = new HashMap<>();
        item2.put("title", "Introduction to Algorithms");
        item2.put("price", 90.99);
        item2.put("quantity", 1);
        cartItems.add(item2);

        double total = cartItems.stream()
                .mapToDouble(i -> (Double) i.get("price") * (Integer) i.get("quantity"))
                .sum();

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("total", total);

        return "cart";
    }
}

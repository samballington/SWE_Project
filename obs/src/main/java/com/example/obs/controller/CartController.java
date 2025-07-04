package com.example.obs.controller;

import com.example.obs.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CartController {

    @Autowired
    private CartService cartService;

    private Long dummyUserId = 1L;

    @GetMapping("/cart")
    public String cartPage(Model model) {
        var cartItems = cartService.getCartItems(dummyUserId);
        double total = cartItems.stream()
                .mapToDouble(item -> (double) item.get("subtotal"))
                .sum();

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("total", total);
        return "cart";
    }

    @PostMapping("/cart/add")
    public String addToCart(@RequestParam Long bookId,
                            @RequestParam(defaultValue = "1") int quantity) {
        cartService.addToCart(dummyUserId, bookId, quantity);
        return "redirect:/";
    }

    @PostMapping("/cart/clear")
    public String clearCart() {
        Long userId = dummyUserId;
        cartService.clearCart(userId);
        return "redirect:/cart";
    }
    
    @GetMapping("/cart/count")
    @ResponseBody
    public int getCartCount() {
        Long userId = dummyUserId;
        return cartService.getCartItemCount(userId);
    }

}

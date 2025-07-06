package com.example.obs.controller;

import com.example.obs.service.CartService;
import com.example.obs.service.UserService;
import com.example.obs.service.BookService;
import com.example.obs.model.User;
import com.example.obs.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CartController {

    @Autowired
    private CartService cartService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private BookService bookService;

    @GetMapping("/cart")
    public String cartPage(Model model) {
        // Only allow authenticated users
        User user = getCurrentAuthenticatedUser();
        if (user == null) {
            return "redirect:/login";
        }
        
        var cartItems = cartService.getCartItems(user);
        java.math.BigDecimal total = cartItems.stream()
                .map(item -> (java.math.BigDecimal) item.get("subtotal"))
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("total", total);
        return "cart";
    }

    @PostMapping("/cart/add")
    public String addToCart(@RequestParam Long bookId,
                            @RequestParam(defaultValue = "1") int quantity) {
        // Only allow authenticated users
        User user = getCurrentAuthenticatedUser();
        if (user == null) {
            return "redirect:/login";
        }
        
        Book book = bookService.getBookById(bookId);
        if (book != null) {
            cartService.addToCart(user, book, quantity);
        }
        return "redirect:/";
    }

    @PostMapping("/cart/clear")
    public String clearCart() {
        // Only allow authenticated users
        User user = getCurrentAuthenticatedUser();
        if (user == null) {
            return "redirect:/login";
        }
        
        cartService.clearCart(user);
        return "redirect:/cart";
    }
    
    @GetMapping("/cart/count")
    @ResponseBody
    public int getCartCount() {
        // Only allow authenticated users
        User user = getCurrentAuthenticatedUser();
        if (user == null) {
            return 0; // Return 0 for unauthenticated users
        }
        
        return cartService.getCartItemCount(user);
    }
    
    private User getCurrentAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            return userService.findByUsername(auth.getName());
        }
        return null; // Return null for unauthenticated users
    }
}

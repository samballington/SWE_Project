package com.example.obs.controller;

import com.example.obs.model.User;
import com.example.obs.service.CartService;
import com.example.obs.service.UserService;
import com.example.obs.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @GetMapping
    public String checkoutPage(Model model) {
        User user = getCurrentUser();
        
        // Get cart items
        List<Map<String, Object>> cartItems = cartService.getCartItems(user);
        if (cartItems.isEmpty()) {
            return "redirect:/cart";
        }
        
        // Calculate totals using BigDecimal for precision
        BigDecimal subtotal = cartItems.stream()
                .map(item -> (BigDecimal) item.get("subtotal"))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal tax = subtotal.multiply(new BigDecimal("0.08"));
        BigDecimal total = subtotal.add(tax);
        
        // Add attributes to model
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("subtotal", subtotal);
        model.addAttribute("tax", tax);
        model.addAttribute("total", total);
        
        // Add user information if authenticated
        if (user.getId() != null) {
            model.addAttribute("user", user);
            model.addAttribute("hasPaymentInfo", user.getPaymentInfo() != null && !user.getPaymentInfo().isEmpty());
            model.addAttribute("hasAddress", user.getAddress() != null && !user.getAddress().isEmpty());
        }
        
        return "checkout";
    }
    
    @PostMapping("/process")
    public String processCheckout(
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String paymentInfo,
            @RequestParam(required = false) boolean saveInfo,
            RedirectAttributes redirectAttributes) {
        
        User user = getCurrentUser();
        
        // Save user information if requested
        if (user.getId() != null && saveInfo) {
            if (address != null && !address.isEmpty()) {
                user.setAddress(address);
            }
            if (paymentInfo != null && !paymentInfo.isEmpty()) {
                user.setPaymentInfo(paymentInfo);
            }
            userService.updateUser(user);
        }
        
        // Generate order number
        String orderNumber = generateOrderNumber();
        
        // Get cart items for order summary
        List<Map<String, Object>> cartItems = cartService.getCartItems(user);
        BigDecimal subtotal = cartItems.stream()
                .map(item -> (BigDecimal) item.get("subtotal"))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal tax = subtotal.multiply(new BigDecimal("0.08"));
        BigDecimal total = subtotal.add(tax);
        
        // Persist order
        orderService.createOrder(user, cartItems, subtotal, tax, total,
                address, paymentInfo, orderNumber);
        
        // Clear the cart
        cartService.clearCart(user);
        
        // Add order information to redirect attributes
        redirectAttributes.addFlashAttribute("orderNumber", orderNumber);
        redirectAttributes.addFlashAttribute("cartItems", cartItems);
        redirectAttributes.addFlashAttribute("subtotal", subtotal);
        redirectAttributes.addFlashAttribute("tax", tax);
        redirectAttributes.addFlashAttribute("total", total);
        redirectAttributes.addFlashAttribute("userEmail", user.getEmail());
        
        return "redirect:/checkout/confirmation";
    }
    
    @GetMapping("/confirmation")
    public String confirmationPage(Model model) {
        // Check if we have order information
        if (!model.containsAttribute("orderNumber")) {
            return "redirect:/";
        }
        
        return "order-confirmation";
    }
    
    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            return userService.findByUsername(auth.getName());
        }
        // For anonymous users, create a temporary user
        User anonymousUser = new User();
        anonymousUser.setUsername("anonymous-" + System.currentTimeMillis());
        return anonymousUser;
    }
    
    private String generateOrderNumber() {
        // Generate a random order number
        return "OBS-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
} 
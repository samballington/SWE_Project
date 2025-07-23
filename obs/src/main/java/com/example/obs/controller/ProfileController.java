package com.example.obs.controller;

import com.example.obs.model.Order;
import com.example.obs.model.User;
import com.example.obs.service.CartService;
import com.example.obs.service.OrderService;
import com.example.obs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private CartService cartService;
    
    @GetMapping
    public String viewProfile(Model model) {
        // Get the current authenticated user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            return "redirect:/login";
        }
        
        User user = userService.findByUsername(auth.getName());
        if (user == null) {
            return "redirect:/login";
        }
        
        // Get order history for the user
        List<Order> orderHistory = orderService.getOrdersByUser(user);
        
        model.addAttribute("user", user);
        model.addAttribute("orderHistory", orderHistory);
        return "profile";
    }
    
    @PostMapping("/reorder")
    public String reorderFromHistory(@RequestParam Long orderId, RedirectAttributes redirectAttributes) {
        // Get the current authenticated user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(auth.getName());
        
        if (user == null) {
            return "redirect:/login";
        }
        
        // Get the order and verify it belongs to the user
        Order order = orderService.getOrderByIdAndUser(orderId, user);
        if (order == null) {
            redirectAttributes.addFlashAttribute("error", "Order not found or access denied");
            return "redirect:/profile";
        }
        
        // Add all items from the order to the cart
        cartService.reorderFromOrder(user, order);
        
        redirectAttributes.addFlashAttribute("success", 
            "Order #" + order.getOrderNumber() + " items have been added to your cart!");
        
        return "redirect:/cart";
    }
    
    @PostMapping("/updatePersonal")
    public String updatePersonalInfo(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String phone,
            RedirectAttributes redirectAttributes) {
        
        // Get the current authenticated user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(auth.getName());
        
        if (user == null) {
            return "redirect:/login";
        }
        
        // Check if email is already used by another user
        if (!user.getEmail().equals(email) && userService.emailExists(email)) {
            redirectAttributes.addFlashAttribute("error", "Email already in use by another account");
            return "redirect:/profile";
        }
        
        // Update user information
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPhone(phone);
        
        userService.updateUser(user);
        redirectAttributes.addFlashAttribute("success", "Personal information updated successfully");
        
        return "redirect:/profile";
    }
    
    @PostMapping("/changePassword")
    public String changePassword(
            @RequestParam String currentPassword,
            @RequestParam String newPassword,
            RedirectAttributes redirectAttributes) {
        
        // Get the current authenticated user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(auth.getName());
        
        if (user == null) {
            return "redirect:/login";
        }
        
        // Verify current password
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            redirectAttributes.addFlashAttribute("error", "Current password is incorrect");
            return "redirect:/profile";
        }
        
        // Update password
        user.setPassword(passwordEncoder.encode(newPassword));
        userService.updateUser(user);
        
        redirectAttributes.addFlashAttribute("success", "Password changed successfully");
        return "redirect:/profile";
    }
    
    @PostMapping("/updateAddress")
    public String updateAddress(
            @RequestParam String address,
            RedirectAttributes redirectAttributes) {
        
        // Get the current authenticated user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(auth.getName());
        
        if (user == null) {
            return "redirect:/login";
        }
        
        // Update address
        user.setAddress(address);
        userService.updateUser(user);
        
        redirectAttributes.addFlashAttribute("success", "Address updated successfully");
        return "redirect:/profile";
    }
    
    @PostMapping("/updatePayment")
    public String updatePayment(
            @RequestParam String paymentInfo,
            RedirectAttributes redirectAttributes) {
        
        // Get the current authenticated user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(auth.getName());
        
        if (user == null) {
            return "redirect:/login";
        }
        
        // Update payment info
        user.setPaymentInfo(paymentInfo);
        userService.updateUser(user);
        
        redirectAttributes.addFlashAttribute("success", "Payment information updated successfully");
        return "redirect:/profile";
    }
    
    @PostMapping("/updatePromotions")
    public String updatePromotions(
            @RequestParam(defaultValue = "false") boolean promotionsSubscribed,
            RedirectAttributes redirectAttributes) {
        
        // Get the current authenticated user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(auth.getName());
        
        if (user == null) {
            return "redirect:/login";
        }
        
        // Update promotions subscription
        user.setPromotionsSubscribed(promotionsSubscribed);
        userService.updateUser(user);
        
        String message = promotionsSubscribed ? 
            "You have successfully subscribed to promotions and special offers!" :
            "You have been unsubscribed from promotional emails.";
            
        redirectAttributes.addFlashAttribute("success", message);
        return "redirect:/profile";
    }
}

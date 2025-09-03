package com.example.obs.controller;

import com.example.obs.service.StripePaymentService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/payment")
public class PaymentController {
    
    @Autowired
    private StripePaymentService stripePaymentService;
    
    @Value("${stripe.public-key}")
    private String stripePublicKey;
    
    @GetMapping("/config")
    @ResponseBody
    public ResponseEntity<?> getStripeConfig() {
        Map<String, String> config = new HashMap<>();
        config.put("publicKey", stripePublicKey);
        return ResponseEntity.ok(config);
    }
    
    @GetMapping("/checkout")
    public String showCheckoutPage(@RequestParam("amount") BigDecimal amount, 
                                   @RequestParam("orderId") Long orderId,
                                   Model model) {
        model.addAttribute("amount", amount);
        model.addAttribute("orderId", orderId);
        model.addAttribute("stripePublicKey", stripePublicKey);
        return "checkout";
    }
    
    @PostMapping("/create-payment-intent")
    @ResponseBody
    public ResponseEntity<?> createPaymentIntent(@RequestBody Map<String, Object> request) {
        try {
            BigDecimal amount = new BigDecimal(request.get("amount").toString());
            String currency = "usd";
            String description = "Order payment - OBS Bookstore";
            
            PaymentIntent paymentIntent = stripePaymentService.createPaymentIntent(amount, currency, description);
            
            Map<String, String> response = new HashMap<>();
            response.put("clientSecret", paymentIntent.getClientSecret());
            
            return ResponseEntity.ok(response);
        } catch (StripeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @PostMapping("/confirm-payment")
    @ResponseBody
    public ResponseEntity<?> confirmPayment(@RequestBody Map<String, Object> request) {
        try {
            String paymentIntentId = request.get("paymentIntentId").toString();
            PaymentIntent paymentIntent = stripePaymentService.retrievePaymentIntent(paymentIntentId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", paymentIntent.getStatus());
            response.put("paymentIntentId", paymentIntent.getId());
            
            return ResponseEntity.ok(response);
        } catch (StripeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
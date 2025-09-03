package com.example.obs.config;

import com.stripe.Stripe;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

@Configuration
public class StripeConfig {
    
    @Value("${stripe.secret-key}")
    private String secretKey;
    
    @Value("${stripe.public-key}")
    private String publicKey;
    
    @PostConstruct
    public void initSecretKey() {
        Stripe.apiKey = secretKey;
    }
    
    @Bean
    public String stripePublicKey() {
        return publicKey;
    }
}
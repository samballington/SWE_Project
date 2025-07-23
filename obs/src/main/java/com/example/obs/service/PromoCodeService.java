package com.example.obs.service;

import com.example.obs.model.PromoCode;
import com.example.obs.repository.PromoCodeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Service
public class PromoCodeService {

    private static final Logger logger = LoggerFactory.getLogger(PromoCodeService.class);

    @Autowired
    private PromoCodeRepository promoCodeRepository;

    public PromoCode validatePromoCode(String code, BigDecimal orderAmount) {
        logger.info("Validating promo code: {} for order amount: {}", code, orderAmount);
        
        if (code == null || code.trim().isEmpty()) {
            return null;
        }
        
        PromoCode promoCode = promoCodeRepository.findByCodeIgnoreCase(code.trim());
        if (promoCode == null) {
            logger.warn("Promo code not found: {}", code);
            return null;
        }
        
        // Check if the promo code is valid
        if (!promoCode.isValid()) {
            logger.warn("Promo code is not valid: {}", code);
            return null;
        }
        
        // Check minimum order amount
        if (promoCode.getMinimumOrderAmount() != null && 
            orderAmount.compareTo(promoCode.getMinimumOrderAmount()) < 0) {
            logger.warn("Order amount {} does not meet minimum requirement {} for promo code: {}", 
                       orderAmount, promoCode.getMinimumOrderAmount(), code);
            return null;
        }
        
        logger.info("Promo code validated successfully: {}", code);
        return promoCode;
    }
    
    public BigDecimal calculateDiscount(PromoCode promoCode, BigDecimal subtotal) {
        if (promoCode == null || promoCode.getDiscountPercentage() == null) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal discount = subtotal.multiply(promoCode.getDiscountPercentage())
                                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        
        logger.info("Calculated discount: {} for promo code: {}", discount, promoCode.getCode());
        return discount;
    }
    
    @Transactional
    public void usePromoCode(String code) {
        PromoCode promoCode = promoCodeRepository.findByCodeIgnoreCase(code);
        if (promoCode != null) {
            promoCode.incrementUsage();
            promoCodeRepository.save(promoCode);
            logger.info("Incremented usage count for promo code: {}", code);
        }
    }
    
    public PromoCode findByCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            return null;
        }
        return promoCodeRepository.findByCodeIgnoreCase(code.trim());
    }
    
    // Helper class for promo code validation results
    public static class PromoCodeValidationResult {
        private boolean valid;
        private String message;
        private PromoCode promoCode;
        private BigDecimal discount;
        
        public PromoCodeValidationResult(boolean valid, String message, PromoCode promoCode, BigDecimal discount) {
            this.valid = valid;
            this.message = message;
            this.promoCode = promoCode;
            this.discount = discount;
        }
        
        // Getters
        public boolean isValid() { return valid; }
        public String getMessage() { return message; }
        public PromoCode getPromoCode() { return promoCode; }
        public BigDecimal getDiscount() { return discount; }
    }
    
    public PromoCodeValidationResult validateAndCalculate(String code, BigDecimal subtotal) {
        if (code == null || code.trim().isEmpty()) {
            return new PromoCodeValidationResult(false, "Please enter a promo code", null, BigDecimal.ZERO);
        }
        
        PromoCode promoCode = validatePromoCode(code, subtotal);
        if (promoCode == null) {
            return new PromoCodeValidationResult(false, "Invalid or expired promo code", null, BigDecimal.ZERO);
        }
        
        BigDecimal discount = calculateDiscount(promoCode, subtotal);
        String message = String.format("Promo code applied! %s%% discount", promoCode.getDiscountPercentage());
        
        return new PromoCodeValidationResult(true, message, promoCode, discount);
    }
} 
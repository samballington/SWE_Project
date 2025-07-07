package com.example.obs.controller;

import com.example.obs.model.PromoCode;
import com.example.obs.repository.PromoCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/admin/promos")
public class AdminPromoCodeController {

    @Autowired
    private PromoCodeRepository promoCodeRepository;

    @GetMapping
    public String listPromoCodes(Model model) {
        model.addAttribute("promoCodes", promoCodeRepository.findAll());
        return "admin/promo-codes";
    }

    @GetMapping("/new")
    public String newPromoCodeForm(Model model) {
        model.addAttribute("promoCode", new PromoCode());
        return "admin/promo-code-form";
    }

    @GetMapping("/edit/{id}")
    public String editPromoCodeForm(@PathVariable Long id, Model model) {
        PromoCode promoCode = promoCodeRepository.findById(id).orElse(null);
        if (promoCode == null) {
            return "redirect:/admin/promos";
        }
        model.addAttribute("promoCode", promoCode);
        return "admin/promo-code-form";
    }

    @PostMapping("/save")
    public String savePromoCode(
            @RequestParam(required = false) Long id,
            @RequestParam String code,
            @RequestParam String description,
            @RequestParam BigDecimal discountPercentage,
            @RequestParam(required = false) BigDecimal minimumOrderAmount,
            @RequestParam(required = false) Integer usageLimit,
            @RequestParam(required = false) boolean isActive) {

        PromoCode promoCode;
        if (id != null) {
            promoCode = promoCodeRepository.findById(id).orElse(new PromoCode());
        } else {
            promoCode = new PromoCode();
        }

        promoCode.setCode(code.toUpperCase());
        promoCode.setDescription(description);
        promoCode.setDiscountPercentage(discountPercentage);
        promoCode.setMinimumOrderAmount(minimumOrderAmount != null ? minimumOrderAmount : BigDecimal.ZERO);
        promoCode.setUsageLimit(usageLimit);
        promoCode.setActive(isActive);

        // Set validity period (30 days from now if new)
        if (id == null) {
            promoCode.setValidFrom(LocalDateTime.now());
            promoCode.setValidUntil(LocalDateTime.now().plusDays(30));
        }

        promoCodeRepository.save(promoCode);
        return "redirect:/admin/promos";
    }

    @GetMapping("/delete/{id}")
    public String deletePromoCode(@PathVariable Long id) {
        promoCodeRepository.deleteById(id);
        return "redirect:/admin/promos";
    }

    @PostMapping("/toggle/{id}")
    public String togglePromoCode(@PathVariable Long id) {
        PromoCode promoCode = promoCodeRepository.findById(id).orElse(null);
        if (promoCode != null) {
            promoCode.setActive(!promoCode.isActive());
            promoCodeRepository.save(promoCode);
        }
        return "redirect:/admin/promos";
    }
} 
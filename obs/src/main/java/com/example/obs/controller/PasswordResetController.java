package com.example.obs.controller;

import com.example.obs.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PasswordResetController {

    private static final Logger logger = LoggerFactory.getLogger(PasswordResetController.class);

    @Autowired
    private UserService userService;

    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam String email, RedirectAttributes redirectAttributes) {
        logger.info("Password reset request for email: {}", email);
        
        boolean emailSent = userService.initiatePasswordReset(email);
        
        if (emailSent) {
            redirectAttributes.addFlashAttribute("success", true);
            redirectAttributes.addFlashAttribute("message", 
                "If an account with that email exists, we've sent password reset instructions to your email.");
        } else {
            redirectAttributes.addFlashAttribute("success", true); // Still show success for security
            redirectAttributes.addFlashAttribute("message", 
                "If an account with that email exists, we've sent password reset instructions to your email.");
        }
        
        return "redirect:/forgot-password";
    }

    @GetMapping("/reset-password")
    public String resetPasswordPage(@RequestParam String token, Model model) {
        // We don't validate the token here, we do it on form submission for better UX
        model.addAttribute("token", token);
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String processResetPassword(
            @RequestParam String token,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            RedirectAttributes redirectAttributes) {
        
        logger.info("Password reset attempt with token: {}", token);
        
        // Validate passwords match
        if (!password.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", true);
            redirectAttributes.addFlashAttribute("message", "Passwords do not match.");
            redirectAttributes.addFlashAttribute("token", token);
            return "redirect:/reset-password?token=" + token;
        }
        
        // Validate password length
        if (password.length() < 6) {
            redirectAttributes.addFlashAttribute("error", true);
            redirectAttributes.addFlashAttribute("message", "Password must be at least 6 characters long.");
            redirectAttributes.addFlashAttribute("token", token);
            return "redirect:/reset-password?token=" + token;
        }
        
        boolean resetSuccessful = userService.resetPassword(token, password);
        
        if (resetSuccessful) {
            redirectAttributes.addFlashAttribute("resetSuccess", true);
            redirectAttributes.addFlashAttribute("message", 
                "Your password has been successfully reset. You can now log in with your new password.");
            return "redirect:/login";
        } else {
            redirectAttributes.addFlashAttribute("error", true);
            redirectAttributes.addFlashAttribute("message", 
                "Invalid or expired reset token. Please request a new password reset.");
            return "redirect:/forgot-password";
        }
    }
} 
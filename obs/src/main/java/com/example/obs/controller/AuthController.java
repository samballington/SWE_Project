package com.example.obs.controller;

import com.example.obs.model.User;
import com.example.obs.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, 
                        @RequestParam(required = false) String logout,
                        Model model) {
        if (error != null) {
            model.addAttribute("loginError", true);
            logger.warn("Login error occurred");
        }
        if (logout != null) {
            model.addAttribute("logoutSuccess", true);
        }
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute User user,
                              BindingResult result,
                              @RequestParam(required = false) String confirmPassword,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        
        logger.info("Processing registration for user: {}", user.getUsername());
        
        // Additional custom validations
        
        // Validate username
        if (userService.usernameExists(user.getUsername())) {
            result.rejectValue("username", "error.user", "Username already exists");
            logger.warn("Registration failed: Username {} already exists", user.getUsername());
        }
        
        // Validate email
        if (userService.emailExists(user.getEmail())) {
            result.rejectValue("email", "error.user", "Email already in use");
            logger.warn("Registration failed: Email {} already in use", user.getEmail());
        }
        
        // Validate password match
        if (confirmPassword == null || !confirmPassword.equals(user.getPassword())) {
            result.rejectValue("password", "error.user", "Passwords don't match");
            logger.warn("Registration failed: Passwords don't match");
        }
        
        // If there are errors, return to the registration form
        if (result.hasErrors()) {
            logger.warn("Registration form has errors: {}", result.getAllErrors());
            return "register";
        }
        
        // Save the new user
        try {
            User savedUser = userService.registerNewUser(user);
            logger.info("User registered successfully: {}", savedUser.getUsername());
            redirectAttributes.addFlashAttribute("registrationSuccess", true);
            redirectAttributes.addFlashAttribute("userEmail", savedUser.getEmail());
            redirectAttributes.addFlashAttribute("message", 
                "Registration successful! Please check your email to verify your account before logging in.");
            return "redirect:/login";
        } catch (Exception e) {
            logger.error("Registration failed with exception", e);
            result.rejectValue("username", "error.user", "Registration failed: " + e.getMessage());
            return "register";
        }
    }
    
    @GetMapping("/verify-email")
    public String verifyEmail(@RequestParam String token, RedirectAttributes redirectAttributes) {
        logger.info("Email verification attempt with token: {}", token);
        
        boolean verified = userService.verifyEmail(token);
        
        if (verified) {
            redirectAttributes.addFlashAttribute("verificationSuccess", true);
            redirectAttributes.addFlashAttribute("message", 
                "Email verified successfully! You can now log in to your account.");
            logger.info("Email verification successful for token: {}", token);
        } else {
            redirectAttributes.addFlashAttribute("verificationError", true);
            redirectAttributes.addFlashAttribute("message", 
                "Invalid or expired verification link. Please try registering again or contact support.");
            logger.warn("Email verification failed for token: {}", token);
        }
        
        return "redirect:/login";
    }
    
    @PostMapping("/resend-verification")
    public String resendVerification(@RequestParam String email, RedirectAttributes redirectAttributes) {
        logger.info("Resend verification email request for: {}", email);
        
        boolean sent = userService.resendVerificationEmail(email);
        
        if (sent) {
            redirectAttributes.addFlashAttribute("resendSuccess", true);
            redirectAttributes.addFlashAttribute("message", 
                "Verification email has been resent. Please check your inbox.");
        } else {
            redirectAttributes.addFlashAttribute("resendError", true);
            redirectAttributes.addFlashAttribute("message", 
                "Unable to resend verification email. Email may already be verified or not found.");
        }
        
        return "redirect:/login";
    }
}

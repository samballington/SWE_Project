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
            return "redirect:/login";
        } catch (Exception e) {
            logger.error("Registration failed with exception", e);
            result.rejectValue("username", "error.user", "Registration failed: " + e.getMessage());
            return "register";
        }
    }
}

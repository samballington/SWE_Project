package com.example.obs.controller;

import com.example.obs.model.User;
import com.example.obs.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String listUsers(Model model){
        model.addAttribute("users", userRepository.findAll());
        return "admin/users";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id){
        userRepository.deleteById(id);
        return "redirect:/admin/users";
    }
} 
package com.example.obs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam String username,
                          @RequestParam String password) {

    	if(username.equals("admin") && password.equals("1234")) {
            return "redirect:/";
        } else {
            return "redirect:/login?error";
        }
    }
}

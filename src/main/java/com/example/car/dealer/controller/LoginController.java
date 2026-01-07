package com.example.car.dealer.controller;

import com.example.car.dealer.dto.RegisterRequest;
import com.example.car.dealer.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // This should match login.html in templates directory
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute RegisterRequest request,
                           Model model) {
        try {
            userService.register(request);
            model.addAttribute("success", "Registration successful. Please login.");
            model.addAttribute("registerRequest", new RegisterRequest());
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("registerRequest", request);
        }
        return "register";
    }
}



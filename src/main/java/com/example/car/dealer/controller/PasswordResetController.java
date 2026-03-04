package com.example.car.dealer.controller;

import com.example.car.dealer.service.PasswordResetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String sendCode(@RequestParam String email, Model model) {
        passwordResetService.sendCode(email);
        model.addAttribute("msg", "If that email exists, we sent a passcode.");
        model.addAttribute("email", email);
        return "reset-password"; // go to reset page
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String email,
                                @RequestParam String code,
                                @RequestParam String newPassword,
                                @RequestParam String confirmPassword,
                                Model model) {
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match.");
            model.addAttribute("email", email);
            return "reset-password";
        }

        try {
            passwordResetService.resetPassword(email, code, newPassword);
            return "redirect:/login?resetSuccess";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("email", email);
            return "reset-password";
        }
    }
}

package com.example.car.dealer.service;

import com.example.car.dealer.entity.PasswordResetCode;
import com.example.car.dealer.entity.User;
import com.example.car.dealer.repository.PasswordResetCodeRepository;
import com.example.car.dealer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final PasswordResetCodeRepository codeRepo;
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService; // you create this (below)

    public void sendCode(String email) {
        // Don’t reveal if email exists (security)
        String code = String.format("%06d", new Random().nextInt(999999));

        PasswordResetCode prc = new PasswordResetCode();
        prc.setEmail(email.trim().toLowerCase());
        prc.setCode(code);
        prc.setExpiresAt(LocalDateTime.now().plusMinutes(10));
        prc.setUsed(false);

        codeRepo.save(prc);

        // send email
        emailService.sendEmail(
                email,
                "Your password reset code",
                "Your passcode is: " + code + "\nThis code expires in 10 minutes."
        );
    }

    @Transactional
    public void resetPassword(String email, String code, String newPassword) {
        PasswordResetCode prc = codeRepo.findTopByEmailOrderByIdDesc(email.trim().toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException("Invalid passcode."));

        if (prc.isUsed()) throw new IllegalArgumentException("This code was already used.");
        if (!prc.getCode().equals(code)) throw new IllegalArgumentException("Invalid passcode.");
        if (prc.getExpiresAt().isBefore(LocalDateTime.now())) throw new IllegalArgumentException("Passcode expired.");

        User user = userRepo.findByEmail(email.trim().toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException("Invalid passcode.")); // don’t reveal user not found

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);

        prc.setUsed(true);
        codeRepo.save(prc);
    }
}

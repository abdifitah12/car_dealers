package com.example.car.dealer.service;

import com.example.car.dealer.dto.RegisterRequest;
import com.example.car.dealer.entity.User;
import com.example.car.dealer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // ✅ read secret from application.properties
    @Value("${app.secret.key}")
    private String appSecretKey;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(RegisterRequest request) {

        // ✅ 1) secret key check
        if (request.getSecretKey() == null || request.getSecretKey().isBlank()) {
            throw new IllegalArgumentException("Secret key is required");
        }
        if (!appSecretKey.equals(request.getSecretKey().trim())) {
            throw new IllegalArgumentException("Invalid secret key");
        }

        // ✅ 2) password check
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        // ✅ 3) duplicates
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        if (userRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Username already exists");
        }

        // ✅ 4) save user
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("CUSTOMER"); // default role

        userRepository.save(user);
    }
}

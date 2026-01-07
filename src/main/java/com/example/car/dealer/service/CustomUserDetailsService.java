package com.example.car.dealer.service;

import com.example.car.dealer.entity.User;
import com.example.car.dealer.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail)
            throws UsernameNotFoundException {

        User user = userRepository.findByEmail(usernameOrEmail)
                .orElseGet(() -> userRepository.findByName(usernameOrEmail)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found")));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), // login with email
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
        );
    }
}

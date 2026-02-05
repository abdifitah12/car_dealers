package com.example.car.dealer.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // If you use forms + POST, keep CSRF enabled. (Default is enabled)
                // If you get 403 on POST, tell me and we’ll fix properly.

                .authorizeHttpRequests(auth -> auth

                        // ✅ Public pages (no login required)
                        .requestMatchers(
                                "/",            // your car list page (HomeCarController root)
                                "/login",
                                "/register",
                                "/forgot-password",
                                "/contact",
                                "/filter",
                                "/models",
                                "/images/**",
                                "/css/**",
                                "/js/**",
                                "/static/**"
                        ).permitAll()

                        // ✅ Protect add-car page (must login)
                        .requestMatchers("/cars/add", "/cars/add/**").authenticated()

                        // ✅ Everything else (optional)
                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/cars/add", true)   // after login go to car list (or change to /cars/add)
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

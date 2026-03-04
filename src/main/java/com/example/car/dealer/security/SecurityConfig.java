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
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
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

                        .requestMatchers("/cars/add", "/cars/add/**").authenticated()

                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/cars/add", true)
                        .permitAll()
                )

                // ✅ Remember-me for 3 hours
                .rememberMe(remember -> remember
                        .key("car-dealer-remember-me-key") // any random string
                        .tokenValiditySeconds(60 * 60 * 3) // 3 hours
                        .rememberMeParameter("remember-me") // matches checkbox name in login.html
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .deleteCookies("JSESSIONID", "remember-me") // ✅ clear both
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

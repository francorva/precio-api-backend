package com.example.price_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @SuppressWarnings("deprecation")
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // 🟢 PERMITE EL ACCESO LIBRE A LAS FOTOS Y A LOS PRODUCTOS
                        .requestMatchers("/foto_producto/**").permitAll()
                        .requestMatchers("/productos/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        // Cambia "/api/productos" por un endpoint real de tu Controller
                        .defaultSuccessUrl("/api/productos", true)
                        .permitAll()
                )
                .httpBasic(httpBasic -> {});

        return http.build();
    }
}
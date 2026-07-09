package org.example.backend.config;

import lombok.RequiredArgsConstructor;
import org.example.backend.entity.AppUser;
import org.example.backend.entity.Role;
import org.example.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
public class AdminBootstrapConfig {

    @Bean
    CommandLineRunner ensureAdminUser(UserRepository userRepository,
                                      PasswordEncoder passwordEncoder,
                                      @Value("${app.admin.email}") String adminEmail,
                                      @Value("${app.admin.password}") String adminPassword,
                                      @Value("${app.admin.name}") String adminName,
                                      @Value("${app.admin.phone}") String adminPhone) {
        return args -> {
            if (!userRepository.existsByEmail(adminEmail)) {
                AppUser admin = AppUser.builder()
                        .name(adminName)
                        .email(adminEmail)
                        .password(passwordEncoder.encode(adminPassword))
                        .phone(adminPhone)
                        .role(Role.ADMIN)
                        .createdAt(LocalDateTime.now())
                        .build();
                userRepository.save(admin);
            }
        };
    }
}


package org.northernarc.assessment4.security;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Task 8: Security Configuration for JWT Authentication
 * Configures Spring Security with JWT token-based authentication
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Task 9: Enables @PreAuthorize role checks on controllers
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    /**
     * Configure the security filter chain
     * - CSRF protection disabled (stateless API)
     * - Public endpoints: /api/auth/login, /api/customers
     * - All other endpoints require authentication
     * - Stateless session management
     * - JWT filter added before authentication filter
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF for stateless APIs
                .anonymous(AbstractHttpConfigurer::disable)
                .exceptionHandling(ex -> ex.authenticationEntryPoint((request, response, authException) ->
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED)))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/login", "/api/customers").permitAll() // Task 8: Open access endpoints
                        .requestMatchers(HttpMethod.POST, "/api/accounts").permitAll() // Task 8: Open access for account creation
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                        .anyRequest().authenticated() // All other endpoints require authentication
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Enforce stateless sessions
                );

        // Add our custom JWT filter before the standard UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Task 8: Password encoder bean using BCrypt
     * BCrypt is a strong password hashing algorithm
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Task 8: Authentication manager bean
     * Used for authenticating user credentials at login
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}

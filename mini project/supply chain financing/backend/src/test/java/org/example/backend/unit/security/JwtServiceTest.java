package org.example.backend.unit.security;

import org.example.backend.security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    private final String secret = "5fN9kBqv3rY8uP2cV7mX1aD4sH6jL0nQ9wE3tR8yU2iO5pA7sD1fG4hJ6kL8zX0";

    @Test
    void generateTokenShouldBeValidForMatchingUser() {
        JwtService jwtService = new JwtService(secret, 60_000L);
        UserDetails user = User.withUsername("user@test.local").password("x").roles("CUSTOMER").build();

        String token = jwtService.generateToken(user, Map.of("role", "CUSTOMER"));

        assertThat(token).isNotBlank();
        assertThat(jwtService.extractUsername(token)).isEqualTo("user@test.local");
        assertThat(jwtService.isTokenValid(token, user)).isTrue();
    }

    @Test
    void isTokenValidShouldFailForDifferentUser() {
        JwtService jwtService = new JwtService(secret, 60_000L);
        UserDetails alice = User.withUsername("alice@test.local").password("x").roles("CUSTOMER").build();
        UserDetails bob = User.withUsername("bob@test.local").password("x").roles("CUSTOMER").build();

        String token = jwtService.generateToken(alice);

        assertThat(jwtService.isTokenValid(token, bob)).isFalse();
    }
}


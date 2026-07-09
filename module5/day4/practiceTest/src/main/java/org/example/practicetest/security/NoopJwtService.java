package org.example.practicetest.security;

import org.springframework.stereotype.Service;

@Service
public class NoopJwtService implements JwtService {
    @Override
    public String extractUsername(String token) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("Invalid token");
        }
        return "user@example.com";
    }

    @Override
    public boolean isTokenValid(String token, String username) {
        return false;
    }
}


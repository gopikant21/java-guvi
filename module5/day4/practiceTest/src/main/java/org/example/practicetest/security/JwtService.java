package org.example.practicetest.security;

public interface JwtService {
    String extractUsername(String token);

    boolean isTokenValid(String token, String username);
}


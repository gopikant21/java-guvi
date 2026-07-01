package org.example.emidefaulter.dto;

public record LoginResponseDTO(
        String token,
        String tokenType,
        long expiresInSeconds
) {
}


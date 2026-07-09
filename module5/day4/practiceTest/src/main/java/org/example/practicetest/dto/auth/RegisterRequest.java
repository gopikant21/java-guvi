package org.example.practicetest.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record RegisterRequest(
        @NotBlank String name,
        @Email @NotBlank String email,
        @Pattern(regexp = "^[A-Za-z0-9]{10}$") String taxId,
        @NotBlank String password
) {
}


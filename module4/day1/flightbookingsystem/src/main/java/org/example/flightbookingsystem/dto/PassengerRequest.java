package org.example.flightbookingsystem.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record PassengerRequest(
        @NotBlank(message = "Name is required") String name,
        @Email(message = "Invalid email format") @NotBlank(message = "Email is required") String email,
        @Pattern(regexp = "^[0-9]{10,15}$", message = "Phone must be 10 to 15 digits") String phone,
        @NotBlank(message = "Passport number is required") String passportNumber
) {
}


package org.example.flightbookingsystem.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.example.flightbookingsystem.model.PaymentMode;

import java.time.LocalDate;

public record PaymentRequest(
        @NotNull(message = "Booking id is required") Long bookingId,
        @Positive(message = "Amount must be greater than 0") double amount,
        @NotNull(message = "Payment date is required") LocalDate paymentDate,
        @NotNull(message = "Payment mode is required") PaymentMode mode
) {
}


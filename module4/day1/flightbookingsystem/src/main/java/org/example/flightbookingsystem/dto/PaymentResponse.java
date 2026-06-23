package org.example.flightbookingsystem.dto;

import org.example.flightbookingsystem.model.PaymentMode;

import java.time.LocalDate;

public record PaymentResponse(
        Integer paymentId,
        double amount,
        LocalDate paymentDate,
        PaymentMode mode,
        Long bookingId
) {
}


package org.example.emidefaulter.dto;

public record CustomerLoanSummaryDTO(
        String customerName,
        String city,
        Long totalLoans,
        Long totalPendingEMIs,
        Double totalPenaltyPaid
) {
}


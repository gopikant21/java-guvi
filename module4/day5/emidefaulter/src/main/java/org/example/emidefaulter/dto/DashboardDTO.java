package org.example.emidefaulter.dto;

public record DashboardDTO(
        long totalCustomers,
        long activeLoans,
        long defaultedLoans,
        double totalOutstandingAmount,
        double totalPenaltyCollected,
        String highestDefaulter,
        double highestOutstandingAmount,
        String cityWithHighestDefaults,
        int averageCreditScore,
        double recoveryRate
) {
}


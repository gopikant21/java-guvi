package org.example.emidefaulter.dto;

public record CustomerPendingEmiDTO(
        Long customerId,
        String customerName,
        Double totalPendingEmi
) {
}


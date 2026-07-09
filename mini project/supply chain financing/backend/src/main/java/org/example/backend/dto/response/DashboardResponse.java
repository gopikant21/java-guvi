package org.example.backend.dto.response;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class DashboardResponse {
    long totalCustomers;
    long totalLoans;
    long pendingLoans;
    long approvedLoans;
    long rejectedLoans;
    long disbursedLoans;
    long closedLoans;
    BigDecimal totalAmountDisbursed;
}


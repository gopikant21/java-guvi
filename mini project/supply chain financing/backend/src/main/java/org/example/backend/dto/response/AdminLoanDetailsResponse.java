package org.example.backend.dto.response;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class AdminLoanDetailsResponse {
    UserProfileResponse customer;
    LoanResponse loan;
    List<RepaymentResponse> repayments;
}


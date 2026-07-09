package org.example.backend.util;

import org.example.backend.dto.response.LoanResponse;
import org.example.backend.dto.response.RepaymentResponse;
import org.example.backend.dto.response.UserProfileResponse;
import org.example.backend.entity.AppUser;
import org.example.backend.entity.Loan;
import org.example.backend.entity.Repayment;

public final class ResponseMapper {

    private ResponseMapper() {
    }

    public static UserProfileResponse toUserProfile(AppUser user) {
        return UserProfileResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();
    }

    public static LoanResponse toLoanResponse(Loan loan) {
        return LoanResponse.builder()
                .id(loan.getId())
                .loanNumber(loan.getLoanNumber())
                .customerId(loan.getCustomer().getId())
                .customerName(loan.getCustomer().getName())
                .amount(loan.getAmount())
                .interestRate(loan.getInterestRate())
                .tenureMonths(loan.getTenureMonths())
                .purpose(loan.getPurpose())
                .status(loan.getStatus())
                .approvedById(loan.getApprovedBy() != null ? loan.getApprovedBy().getId() : null)
                .approvedDate(loan.getApprovedDate())
                .disbursedDate(loan.getDisbursedDate())
                .remainingAmount(loan.getRemainingAmount())
                .rejectionReason(loan.getRejectionReason())
                .createdAt(loan.getCreatedAt())
                .build();
    }

    public static RepaymentResponse toRepaymentResponse(Repayment repayment) {
        return RepaymentResponse.builder()
                .id(repayment.getId())
                .loanId(repayment.getLoan().getId())
                .amount(repayment.getAmount())
                .paymentDate(repayment.getPaymentDate())
                .paymentMode(repayment.getPaymentMode())
                .remarks(repayment.getRemarks())
                .build();
    }
}


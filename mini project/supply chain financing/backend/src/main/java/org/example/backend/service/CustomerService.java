package org.example.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.dto.request.LoanApplicationRequest;
import org.example.backend.dto.request.RepaymentRequest;
import org.example.backend.dto.request.UpdateProfileRequest;
import org.example.backend.dto.response.LoanResponse;
import org.example.backend.dto.response.RepaymentResponse;
import org.example.backend.dto.response.UserProfileResponse;
import org.example.backend.entity.*;
import org.example.backend.exception.BadRequestException;
import org.example.backend.exception.ResourceNotFoundException;
import org.example.backend.repository.LoanRepository;
import org.example.backend.repository.RepaymentRepository;
import org.example.backend.repository.UserRepository;
import org.example.backend.util.ResponseMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {

    private final UserRepository userRepository;
    private final LoanRepository loanRepository;
    private final RepaymentRepository repaymentRepository;

    public UserProfileResponse getProfile(String email) {
        return ResponseMapper.toUserProfile(getCustomerByEmail(email));
    }

    @Transactional
    public UserProfileResponse updateProfile(String email, UpdateProfileRequest request) {
        AppUser customer = getCustomerByEmail(email);
        customer.setName(request.getName());
        customer.setPhone(request.getPhone());
        return ResponseMapper.toUserProfile(userRepository.save(customer));
    }

    @Transactional
    public LoanResponse applyLoan(String email, LoanApplicationRequest request) {
        log.info("Creating loan request for customer={} amount={}", email, request.getAmount());
        AppUser customer = getCustomerByEmail(email);

        BigDecimal totalPayable = calculateTotalPayable(
                request.getAmount(), request.getInterestRate(), request.getTenureMonths());

        Loan loan = Loan.builder()
                .loanNumber("LN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .customer(customer)
                .amount(request.getAmount().setScale(2, RoundingMode.HALF_UP))
                .interestRate(request.getInterestRate().setScale(2, RoundingMode.HALF_UP))
                .tenureMonths(request.getTenureMonths())
                .purpose(request.getPurpose())
                .status(LoanStatus.PENDING)
                .remainingAmount(totalPayable)
                .createdAt(java.time.LocalDateTime.now())
                .build();

        return ResponseMapper.toLoanResponse(loanRepository.save(loan));
    }

    public List<LoanResponse> getMyLoans(String email) {
        AppUser customer = getCustomerByEmail(email);
        return loanRepository.findByCustomerOrderByCreatedAtDesc(customer)
                .stream()
                .map(ResponseMapper::toLoanResponse)
                .toList();
    }

    public LoanResponse getMyLoanDetails(String email, Long loanId) {
        AppUser customer = getCustomerByEmail(email);
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found"));

        if (!loan.getCustomer().getId().equals(customer.getId())) {
            throw new BadRequestException("Loan does not belong to customer");
        }

        return ResponseMapper.toLoanResponse(loan);
    }

    @Transactional
    public LoanResponse repayLoan(String email, Long loanId, RepaymentRequest request) {
        log.info("Processing repayment customer={} loanId={} amount={}", email, loanId, request.getAmount());
        AppUser customer = getCustomerByEmail(email);
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found"));

        if (!loan.getCustomer().getId().equals(customer.getId())) {
            throw new BadRequestException("Loan does not belong to customer");
        }

        if (!(loan.getStatus() == LoanStatus.DISBURSED || loan.getStatus() == LoanStatus.PARTIALLY_PAID)) {
            throw new BadRequestException("Loan is not eligible for repayment");
        }

        BigDecimal amount = request.getAmount().setScale(2, RoundingMode.HALF_UP);
        if (amount.compareTo(loan.getRemainingAmount()) > 0) {
            throw new BadRequestException("Repayment amount exceeds remaining amount");
        }

        Repayment repayment = Repayment.builder()
                .loan(loan)
                .amount(amount)
                .paymentDate(LocalDate.now())
                .paymentMode(request.getPaymentMode())
                .remarks(request.getRemarks())
                .build();
        repaymentRepository.save(repayment);

        BigDecimal remaining = loan.getRemainingAmount().subtract(amount).setScale(2, RoundingMode.HALF_UP);
        loan.setRemainingAmount(remaining);
        if (remaining.compareTo(BigDecimal.ZERO) == 0) {
            loan.setStatus(LoanStatus.FULLY_PAID);
            log.info("Loan fully paid loanId={} customer={}", loanId, email);
        } else {
            loan.setStatus(LoanStatus.PARTIALLY_PAID);
            log.debug("Loan partially paid loanId={} remaining={}", loanId, remaining);
        }

        return ResponseMapper.toLoanResponse(loanRepository.save(loan));
    }

    public List<RepaymentResponse> getRepayments(String email, Long loanId) {
        AppUser customer = getCustomerByEmail(email);
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found"));

        if (!loan.getCustomer().getId().equals(customer.getId())) {
            throw new BadRequestException("Loan does not belong to customer");
        }

        return repaymentRepository.findByLoanOrderByPaymentDateDesc(loan)
                .stream()
                .map(ResponseMapper::toRepaymentResponse)
                .toList();
    }

    private AppUser getCustomerByEmail(String email) {
        AppUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getRole() != Role.CUSTOMER) {
            throw new BadRequestException("Not a customer account");
        }
        return user;
    }

    private BigDecimal calculateTotalPayable(BigDecimal principal, BigDecimal interestRate, int tenureMonths) {
        BigDecimal interest = principal
                .multiply(interestRate)
                .multiply(BigDecimal.valueOf(tenureMonths))
                .divide(BigDecimal.valueOf(1200), 2, RoundingMode.HALF_UP);
        return principal.add(interest).setScale(2, RoundingMode.HALF_UP);
    }
}


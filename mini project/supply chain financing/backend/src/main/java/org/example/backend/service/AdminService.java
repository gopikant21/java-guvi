package org.example.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.dto.response.*;
import org.example.backend.entity.AppUser;
import org.example.backend.entity.Loan;
import org.example.backend.entity.LoanStatus;
import org.example.backend.entity.Role;
import org.example.backend.exception.BadRequestException;
import org.example.backend.exception.ResourceNotFoundException;
import org.example.backend.repository.LoanRepository;
import org.example.backend.repository.RepaymentRepository;
import org.example.backend.repository.UserRepository;
import org.example.backend.util.ResponseMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {

    private final UserRepository userRepository;
    private final LoanRepository loanRepository;
    private final RepaymentRepository repaymentRepository;

    public DashboardResponse dashboard() {
        log.debug("Building admin dashboard metrics");
        long totalCustomers = userRepository.findByRole(Role.CUSTOMER).size();
        List<Loan> allLoans = loanRepository.findAll();
        BigDecimal totalDisbursed = allLoans.stream()
                .filter(loan -> loan.getStatus() == LoanStatus.DISBURSED
                        || loan.getStatus() == LoanStatus.PARTIALLY_PAID
                        || loan.getStatus() == LoanStatus.FULLY_PAID
                        || loan.getStatus() == LoanStatus.CLOSED)
                .map(Loan::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return DashboardResponse.builder()
                .totalCustomers(totalCustomers)
                .totalLoans(allLoans.size())
                .pendingLoans(loanRepository.countByStatus(LoanStatus.PENDING))
                .approvedLoans(loanRepository.countByStatus(LoanStatus.APPROVED))
                .rejectedLoans(loanRepository.countByStatus(LoanStatus.REJECTED))
                .disbursedLoans(loanRepository.countByStatus(LoanStatus.DISBURSED))
                .closedLoans(loanRepository.countByStatus(LoanStatus.CLOSED))
                .totalAmountDisbursed(totalDisbursed)
                .build();
    }

    public List<UserProfileResponse> getAllCustomers() {
        return userRepository.findByRole(Role.CUSTOMER)
                .stream()
                .map(ResponseMapper::toUserProfile)
                .toList();
    }

    public CustomerDetailsResponse getCustomerDetails(Long customerId) {
        AppUser customer = userRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        if (customer.getRole() != Role.CUSTOMER) {
            throw new BadRequestException("User is not a customer");
        }

        List<LoanResponse> loans = loanRepository.findByCustomerId(customerId)
                .stream()
                .map(ResponseMapper::toLoanResponse)
                .toList();

        return CustomerDetailsResponse.builder()
                .customer(ResponseMapper.toUserProfile(customer))
                .loans(loans)
                .build();
    }

    public List<LoanResponse> getAllLoans(LoanStatus status, Long customerId) {
        List<Loan> loans;
        if (status != null && customerId != null) {
            loans = loanRepository.findByStatusAndCustomerId(status, customerId);
        } else if (status != null) {
            loans = loanRepository.findByStatus(status);
        } else if (customerId != null) {
            loans = loanRepository.findByCustomerId(customerId);
        } else {
            loans = loanRepository.findAll();
        }

        return loans.stream().map(ResponseMapper::toLoanResponse).toList();
    }

    public AdminLoanDetailsResponse getLoanDetails(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found"));

        return AdminLoanDetailsResponse.builder()
                .customer(ResponseMapper.toUserProfile(loan.getCustomer()))
                .loan(ResponseMapper.toLoanResponse(loan))
                .repayments(repaymentRepository.findByLoanOrderByPaymentDateDesc(loan)
                        .stream()
                        .map(ResponseMapper::toRepaymentResponse)
                        .toList())
                .build();
    }

    @Transactional
    public LoanResponse approveLoan(Long loanId, String adminEmail) {
        log.info("Approving loanId={} by admin={}", loanId, adminEmail);
        Loan loan = fetchLoan(loanId);
        if (loan.getStatus() != LoanStatus.PENDING) {
            throw new BadRequestException("Only pending loan can be approved");
        }

        AppUser admin = getAdmin(adminEmail);
        loan.setStatus(LoanStatus.APPROVED);
        loan.setApprovedBy(admin);
        loan.setApprovedDate(LocalDate.now());
        loan.setRejectionReason(null);

        return ResponseMapper.toLoanResponse(loanRepository.save(loan));
    }

    @Transactional
    public LoanResponse rejectLoan(Long loanId, String reason) {
        log.info("Rejecting loanId={} reason={}", loanId, reason);
        Loan loan = fetchLoan(loanId);
        if (loan.getStatus() != LoanStatus.PENDING) {
            throw new BadRequestException("Only pending loan can be rejected");
        }

        loan.setStatus(LoanStatus.REJECTED);
        loan.setRejectionReason(reason);
        return ResponseMapper.toLoanResponse(loanRepository.save(loan));
    }

    @Transactional
    public LoanResponse disburseLoan(Long loanId) {
        log.info("Disbursing loanId={}", loanId);
        Loan loan = fetchLoan(loanId);
        if (loan.getStatus() != LoanStatus.APPROVED) {
            throw new BadRequestException("Only approved loan can be disbursed");
        }

        loan.setStatus(LoanStatus.DISBURSED);
        loan.setDisbursedDate(LocalDate.now());
        return ResponseMapper.toLoanResponse(loanRepository.save(loan));
    }

    public List<RepaymentResponse> getLoanRepayments(Long loanId) {
        Loan loan = fetchLoan(loanId);
        return repaymentRepository.findByLoanOrderByPaymentDateDesc(loan)
                .stream()
                .map(ResponseMapper::toRepaymentResponse)
                .toList();
    }

    private Loan fetchLoan(Long loanId) {
        return loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found"));
    }

    private AppUser getAdmin(String email) {
        AppUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found"));

        if (user.getRole() != Role.ADMIN) {
            throw new BadRequestException("Current user is not admin");
        }
        return user;
    }
}


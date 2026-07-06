package org.example.emidefaulter.serviceimpl;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.emidefaulter.dto.CityOutstandingDTO;
import org.example.emidefaulter.dto.CustomerLoanSummaryDTO;
import org.example.emidefaulter.dto.CustomerPendingEmiDTO;
import org.example.emidefaulter.dto.DashboardDTO;
import org.example.emidefaulter.entity.Customer;
import org.example.emidefaulter.entity.EmiPayment;
import org.example.emidefaulter.entity.Loan;
import org.example.emidefaulter.entity.Penalty;
import org.example.emidefaulter.exception.CustomerNotFoundException;
import org.example.emidefaulter.exception.EmiPaymentNotFoundException;
import org.example.emidefaulter.exception.LoanNotFoundException;
import org.example.emidefaulter.exception.PenaltyNotFoundException;
import org.example.emidefaulter.repository.CustomerRepository;
import org.example.emidefaulter.repository.EmiPaymentRepository;
import org.example.emidefaulter.repository.LoanRepository;
import org.example.emidefaulter.repository.PenaltyRepository;
import org.example.emidefaulter.service.LoanRecoveryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoanRecoveryServiceImpl implements LoanRecoveryService {

    private final CustomerRepository customerRepository;
    private final LoanRepository loanRepository;
    private final EmiPaymentRepository emiPaymentRepository;
    private final PenaltyRepository penaltyRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Customer registerCustomer(Customer customer) {
        log.debug("Registering new customer with email: {}", customer.getEmail());
        try {
            customer.setPassword(passwordEncoder.encode(customer.getPassword()));
            Customer savedCustomer = customerRepository.save(customer);
            log.info("Customer registered successfully with ID: {} and email: {}", savedCustomer.getCustomerId(), customer.getEmail());
            return savedCustomer;
        } catch (Exception e) {
            log.error("Error registering customer with email: {}", customer.getEmail(), e);
            throw e;
        }
    }

    @Override
    public void deleteCustomer(Long customerId) {
        log.debug("Attempting to delete customer with ID: {}", customerId);
        try {
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + customerId));
            customerRepository.delete(customer);
            log.info("Customer deleted successfully with ID: {}", customerId);
        } catch (CustomerNotFoundException e) {
            log.warn("Customer not found with ID: {}", customerId);
            throw e;
        } catch (Exception e) {
            log.error("Error deleting customer with ID: {}", customerId, e);
            throw e;
        }
    }

    @Override
    public void deleteLoan(Long loanId) {
        log.debug("Attempting to delete loan with ID: {}", loanId);
        try {
            Loan loan = loanRepository.findById(loanId)
                    .orElseThrow(() -> new LoanNotFoundException("Loan not found with id: " + loanId));
            loanRepository.delete(loan);
            log.info("Loan deleted successfully with ID: {}", loanId);
        } catch (LoanNotFoundException e) {
            log.warn("Loan not found with ID: {}", loanId);
            throw e;
        } catch (Exception e) {
            log.error("Error deleting loan with ID: {}", loanId, e);
            throw e;
        }
    }

    @Override
    @Transactional
    public Loan updateLoanStatus(Long loanId, String loanStatus) {
        log.debug("Updating loan status for loan ID: {} to status: {}", loanId, loanStatus);
        try {
            Loan loan = loanRepository.findById(loanId)
                    .orElseThrow(() -> new LoanNotFoundException("Loan not found with id: " + loanId));
            loan.setLoanStatus(loanStatus);
            log.info("Loan status updated successfully for ID: {} to status: {}", loanId, loanStatus);
            return loan;
        } catch (LoanNotFoundException e) {
            log.warn("Loan not found with ID: {}", loanId);
            throw e;
        } catch (Exception e) {
            log.error("Error updating loan status for ID: {}", loanId, e);
            throw e;
        }
    }

    @Override
    @Transactional
    public Penalty generatePenalty(Long paymentId, Double penaltyAmount, String reason) {
        log.debug("Generating penalty for payment ID: {} with amount: {} and reason: {}", paymentId, penaltyAmount, reason);
        try {
            EmiPayment payment = emiPaymentRepository.findById(paymentId)
                    .orElseThrow(() -> new EmiPaymentNotFoundException("EMI payment not found with id: " + paymentId));

            Penalty penalty = Penalty.builder()
                    .payment(payment)
                    .penaltyAmount(penaltyAmount)
                    .penaltyReason(reason)
                    .generatedDate(LocalDate.now())
                    .build();

            Penalty savedPenalty = penaltyRepository.save(penalty);
            log.info("Penalty generated successfully with ID: {} for payment ID: {}", savedPenalty.getPenaltyId(), paymentId);
            return savedPenalty;
        } catch (EmiPaymentNotFoundException e) {
            log.warn("EMI payment not found with ID: {}", paymentId);
            throw e;
        } catch (Exception e) {
            log.error("Error generating penalty for payment ID: {}", paymentId, e);
            throw e;
        }
    }

    @Override
    public List<Customer> findCustomersByCity(String city) {
        return customerRepository.findByCity(city);
    }

    @Override
    public List<Loan> findLoansByStatus(String status) {
        return loanRepository.findByLoanStatus(status);
    }

    @Override
    public List<Loan> findLoansByAmountGreaterThan(Double amount) {
        return loanRepository.findByLoanAmountGreaterThan(amount);
    }

    @Override
    public List<EmiPayment> findPaymentsByStatus(String status) {
        return emiPaymentRepository.findByPaymentStatus(status);
    }

    @Override
    public List<Customer> findCustomersByCreditScoreLessThan(Integer score) {
        return customerRepository.findByCreditScoreLessThan(score);
    }

    @Override
    public List<Customer> findHighRiskCustomers(long missedThreshold) {
        return customerRepository.findHighRiskCustomers(missedThreshold);
    }

    @Override
    public List<CityOutstandingDTO> getOutstandingAmountCityWise() {
        return loanRepository.getOutstandingAmountCityWise();
    }

    @Override
    public List<Customer> getCustomersWithMultipleLoanTypes() {
        return customerRepository.findCustomersWithMultipleLoanTypes();
    }

    @Override
    public Penalty getLatestPenaltyGenerated() {
        return penaltyRepository.findLatestPenaltyRecords(PageRequest.of(0, 1))
                .stream()
                .findFirst()
                .orElseThrow(() -> new PenaltyNotFoundException("No penalty records found"));
    }

    @Override
    public List<Loan> getLoansWithoutMissedEmi() {
        return loanRepository.findLoansWithoutMissedEmi();
    }

    @Override
    public List<CustomerPendingEmiDTO> getTopDefaulters(int limit) {
        return customerRepository.findTopDefaulters(PageRequest.of(0, limit));
    }

    @Override
    @Transactional
    public int increaseInterestRate(double percentage) {
        log.debug("Increasing interest rate by percentage: {}", percentage);
        try {
            if (percentage <= 0) {
                log.error("Invalid percentage value: {}. Percentage must be greater than zero", percentage);
                throw new ValidationException("Percentage must be greater than zero");
            }
            int updatedRows = loanRepository.increaseInterestRate(percentage);
            log.info("Interest rate increased successfully for {} loans by percentage: {}", updatedRows, percentage);
            return updatedRows;
        } catch (Exception e) {
            log.error("Error increasing interest rate by percentage: {}", percentage, e);
            throw e;
        }
    }

    @Override
    public List<CustomerLoanSummaryDTO> getCustomerLoanSummary() {
        log.debug("Fetching customer loan summary");
        try {
            List<CustomerLoanSummaryDTO> summary = customerRepository.getCustomerLoanSummary();
            log.info("Customer loan summary fetched successfully with {} records", summary.size());
            return summary;
        } catch (Exception e) {
            log.error("Error fetching customer loan summary", e);
            throw e;
        }
    }

    @Override
    public List<Loan> getLoansForLoggedInCustomer(String email) {
        log.debug("Fetching loans for logged-in customer with email: {}", email);
        try {
            List<Loan> loans = loanRepository.findLoansByCustomerEmail(email);
            log.info("Loans fetched successfully for customer with email: {}, total loans: {}", email, loans.size());
            return loans;
        } catch (Exception e) {
            log.error("Error fetching loans for customer with email: {}", email, e);
            throw e;
        }
    }

    @Override
    public List<EmiPayment> getEmiHistoryForLoggedInCustomer(String email) {
        log.debug("Fetching EMI history for logged-in customer with email: {}", email);
        try {
            List<EmiPayment> emiHistory = emiPaymentRepository.findEmiHistoryByCustomerEmail(email);
            log.info("EMI history fetched successfully for customer with email: {}, total payments: {}", email, emiHistory.size());
            return emiHistory;
        } catch (Exception e) {
            log.error("Error fetching EMI history for customer with email: {}", email, e);
            throw e;
        }
    }

    @Override
    public Page<Loan> getLoans(Pageable pageable) {
        log.debug("Fetching loans with pagination - page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());
        Pageable sortedPageable = pageable.getSort().isUnsorted()
                ? PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "emiAmount"))
                : pageable;
        return loanRepository.findAll(sortedPageable);
    }

    @Override
    public DashboardDTO getDashboard() {
        log.debug("Generating dashboard metrics");
        try {
            long totalCustomers = customerRepository.count();
            long activeLoans = loanRepository.countByLoanStatus("ACTIVE");
            long defaultedLoans = loanRepository.countByLoanStatus("DEFAULTED");

            double totalOutstandingAmount = getOutstandingAmountCityWise().stream()
                    .mapToDouble(CityOutstandingDTO::totalOutstandingEmi)
                    .sum();

            double totalPenaltyCollected = nullSafeDouble(penaltyRepository.sumTotalPenaltyCollected());

            List<CustomerPendingEmiDTO> defaulters = getTopDefaulters(1);
            String highestDefaulter = defaulters.isEmpty() ? "N/A" : defaulters.get(0).customerName();
            double highestOutstandingAmount = defaulters.isEmpty() ? 0.0 : defaulters.get(0).totalPendingEmi();

            String cityWithHighestDefaults = loanRepository.findCityWithHighestDefaults(PageRequest.of(0, 1))
                    .stream()
                    .findFirst()
                    .orElse("N/A");

            int averageCreditScore = (int) Math.round(nullSafeDouble(customerRepository.findAverageCreditScore()));

            double totalPaid = nullSafeDouble(emiPaymentRepository.sumTotalAmountPaid());
            double totalDue = nullSafeDouble(emiPaymentRepository.sumTotalDueAmount());
            double recoveryRate = totalDue == 0 ? 0.0 : (totalPaid / totalDue) * 100.0;

            DashboardDTO dashboard = new DashboardDTO(
                    totalCustomers,
                    activeLoans,
                    defaultedLoans,
                    round(totalOutstandingAmount),
                    round(totalPenaltyCollected),
                    highestDefaulter,
                    round(highestOutstandingAmount),
                    cityWithHighestDefaults,
                    averageCreditScore,
                    round(recoveryRate)
            );

            log.info("Dashboard metrics generated - Total Customers: {}, Active Loans: {}, Recovery Rate: {}%",
                    totalCustomers, activeLoans, round(recoveryRate));
            return dashboard;
        } catch (Exception e) {
            log.error("Error generating dashboard metrics", e);
            throw e;
        }
    }

    @Override
    @Transactional
    public void runMissedEmiScheduler() {
        log.info("Starting missed EMI scheduler");
        try {
            List<EmiPayment> overduePayments = emiPaymentRepository.findOverduePendingPayments(LocalDate.now());
            log.debug("Found {} overdue EMI payments", overduePayments.size());

            for (EmiPayment payment : overduePayments) {
                payment.setPaymentStatus("MISSED");

                boolean alreadyPenalized = penaltyRepository.findFirstByPaymentPaymentId(payment.getPaymentId()).isPresent();
                if (!alreadyPenalized) {
                    double penaltyAmount = Math.max(payment.getLoan().getEmiAmount() * 0.02, 100.0);
                    Penalty penalty = Penalty.builder()
                            .payment(payment)
                            .penaltyAmount(round(penaltyAmount))
                            .penaltyReason("Auto-generated for missed EMI payment")
                            .generatedDate(LocalDate.now())
                            .build();
                    penaltyRepository.save(penalty);
                    log.debug("Auto-generated penalty for payment ID: {}", payment.getPaymentId());
                }

                Loan loan = payment.getLoan();
                List<EmiPayment> payments = loan.getEmiPayments().stream()
                        .sorted(Comparator.comparing(EmiPayment::getDueDate).reversed())
                        .toList();

                if (payments.size() >= 3
                        && "MISSED".equals(payments.get(0).getPaymentStatus())
                        && "MISSED".equals(payments.get(1).getPaymentStatus())
                        && "MISSED".equals(payments.get(2).getPaymentStatus())) {
                    loan.setLoanStatus("DEFAULTED");
                    log.warn("Loan ID: {} marked as DEFAULTED due to 3 consecutive missed EMI payments", loan.getLoanId());
                }
            }

            log.info("Missed EMI scheduler completed successfully");
        } catch (Exception e) {
            log.error("Error in missed EMI scheduler", e);
            throw e;
        }
    }

    private double nullSafeDouble(Double value) {
        return value == null ? 0.0 : value;
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}


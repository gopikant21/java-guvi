package org.example.emidefaulter.serviceimpl;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
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
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        return customerRepository.save(customer);
    }

    @Override
    public void deleteCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + customerId));
        customerRepository.delete(customer);
    }

    @Override
    public void deleteLoan(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException("Loan not found with id: " + loanId));
        loanRepository.delete(loan);
    }

    @Override
    @Transactional
    public Loan updateLoanStatus(Long loanId, String loanStatus) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException("Loan not found with id: " + loanId));
        loan.setLoanStatus(loanStatus);
        return loan;
    }

    @Override
    @Transactional
    public Penalty generatePenalty(Long paymentId, Double penaltyAmount, String reason) {
        EmiPayment payment = emiPaymentRepository.findById(paymentId)
                .orElseThrow(() -> new EmiPaymentNotFoundException("EMI payment not found with id: " + paymentId));

        Penalty penalty = Penalty.builder()
                .payment(payment)
                .penaltyAmount(penaltyAmount)
                .penaltyReason(reason)
                .generatedDate(LocalDate.now())
                .build();

        return penaltyRepository.save(penalty);
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
        if (percentage <= 0) {
            throw new ValidationException("Percentage must be greater than zero");
        }
        return loanRepository.increaseInterestRate(percentage);
    }

    @Override
    public Page<Loan> getLoans(Pageable pageable) {
        Pageable sortedPageable = pageable.getSort().isUnsorted()
                ? PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "emiAmount"))
                : pageable;
        return loanRepository.findAll(sortedPageable);
    }

    @Override
    public List<CustomerLoanSummaryDTO> getCustomerLoanSummary() {
        return customerRepository.getCustomerLoanSummary();
    }

    @Override
    public List<Loan> getLoansForLoggedInCustomer(String email) {
        return loanRepository.findLoansByCustomerEmail(email);
    }

    @Override
    public List<EmiPayment> getEmiHistoryForLoggedInCustomer(String email) {
        return emiPaymentRepository.findEmiHistoryByCustomerEmail(email);
    }

    @Override
    public DashboardDTO getDashboard() {
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

        return new DashboardDTO(
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
    }

    @Override
    @Transactional
    public void runMissedEmiScheduler() {
        List<EmiPayment> overduePayments = emiPaymentRepository.findOverduePendingPayments(LocalDate.now());

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
            }
        }
    }

    private double nullSafeDouble(Double value) {
        return value == null ? 0.0 : value;
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}


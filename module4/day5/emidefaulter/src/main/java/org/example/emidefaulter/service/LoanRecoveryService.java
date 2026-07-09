package org.example.emidefaulter.service;

import org.example.emidefaulter.dto.CityOutstandingDTO;
import org.example.emidefaulter.dto.CustomerLoanSummaryDTO;
import org.example.emidefaulter.dto.CustomerPendingEmiDTO;
import org.example.emidefaulter.dto.DashboardDTO;
import org.example.emidefaulter.entity.Customer;
import org.example.emidefaulter.entity.EmiPayment;
import org.example.emidefaulter.entity.Loan;
import org.example.emidefaulter.entity.LoanStatus;
import org.example.emidefaulter.entity.Penalty;
import org.example.emidefaulter.entity.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LoanRecoveryService {

    Customer registerCustomer(Customer customer);

    void deleteCustomer(Long customerId);

    void deleteLoan(Long loanId);

    Loan updateLoanStatus(Long loanId, LoanStatus loanStatus);

    Penalty generatePenalty(Long paymentId, Double penaltyAmount, String reason);

    List<Customer> findCustomersByCity(String city);

    List<Loan> findLoansByStatus(LoanStatus status);

    List<Loan> findLoansByAmountGreaterThan(Double amount);

    List<EmiPayment> findPaymentsByStatus(PaymentStatus status);

    List<Customer> findCustomersByCreditScoreLessThan(Integer score);

    List<Customer> findHighRiskCustomers(long missedThreshold);

    List<CityOutstandingDTO> getOutstandingAmountCityWise();

    List<Customer> getCustomersWithMultipleLoanTypes();

    Penalty getLatestPenaltyGenerated();

    List<Loan> getLoansWithoutMissedEmi();

    List<CustomerPendingEmiDTO> getTopDefaulters(int limit);

    int increaseInterestRate(double percentage);

    Page<Loan> getLoans(Pageable pageable);

    List<CustomerLoanSummaryDTO> getCustomerLoanSummary();

    List<Loan> getLoansForLoggedInCustomer(String email);

    List<EmiPayment> getEmiHistoryForLoggedInCustomer(String email);

    DashboardDTO getDashboard();

    void runMissedEmiScheduler();
}


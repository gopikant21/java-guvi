package org.example.emidefaulter.repository;

import org.example.emidefaulter.dto.CityOutstandingDTO;
import org.example.emidefaulter.dto.CustomerPendingEmiDTO;
import org.example.emidefaulter.entity.Customer;
import org.example.emidefaulter.entity.CustomerRole;
import org.example.emidefaulter.entity.EmiPayment;
import org.example.emidefaulter.entity.Loan;
import org.example.emidefaulter.entity.LoanStatus;
import org.example.emidefaulter.entity.LoanType;
import org.example.emidefaulter.entity.Penalty;
import org.example.emidefaulter.entity.PaymentStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RepositoryLayerDataJpaTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private EmiPaymentRepository emiPaymentRepository;

    @Autowired
    private PenaltyRepository penaltyRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void customerRepositoryFindByEmailAndCityWorkAsExpected() {
        Customer customer = createCustomer("Alice", "alice@example.com", "Mumbai", 760);
        customerRepository.save(customer);

        assertTrue(customerRepository.findByEmail("alice@example.com").isPresent());
        assertEquals(1, customerRepository.findByCity("Mumbai").size());
    }

    @Test
    void customerRepositoryFindHighRiskCustomersReturnsOnlyCustomersAboveThreshold() {
        Customer risky = createCustomer("Risky", "risky@example.com", "Pune", 620);
        Loan riskyLoan = createLoan(LoanType.PERSONAL, LoanStatus.ACTIVE, 1500.0, 12.0);
        addLoan(risky, riskyLoan);
        addPayment(riskyLoan, createPayment(LocalDate.now().minusDays(10), PaymentStatus.MISSED, 0.0));
        addPayment(riskyLoan, createPayment(LocalDate.now().minusDays(5), PaymentStatus.MISSED, 0.0));
        addPayment(riskyLoan, createPayment(LocalDate.now().minusDays(2), PaymentStatus.PAID, 1500.0));

        Customer safe = createCustomer("Safe", "safe@example.com", "Pune", 740);
        Loan safeLoan = createLoan(LoanType.HOME, LoanStatus.ACTIVE, 2500.0, 9.0);
        addLoan(safe, safeLoan);
        addPayment(safeLoan, createPayment(LocalDate.now().minusDays(8), PaymentStatus.MISSED, 0.0));

        customerRepository.saveAll(List.of(risky, safe));

        List<Customer> highRisk = customerRepository.findHighRiskCustomers(1L);

        assertEquals(1, highRisk.size());
        assertEquals("Risky", highRisk.get(0).getCustomerName());
    }

    @Test
    void loanRepositoryIncreaseInterestRateUpdatesOnlyActivePersonalLoans() {
        Customer customer = createCustomer("John", "john@example.com", "Mumbai", 730);

        Loan personalActive = createLoan(LoanType.PERSONAL, LoanStatus.ACTIVE, 2000.0, 10.0);
        Loan homeActive = createLoan(LoanType.HOME, LoanStatus.ACTIVE, 3000.0, 8.0);
        Loan personalClosed = createLoan(LoanType.PERSONAL, LoanStatus.CLOSED, 2500.0, 11.0);

        addLoan(customer, personalActive);
        addLoan(customer, homeActive);
        addLoan(customer, personalClosed);

        customerRepository.save(customer);

        int updatedRows = loanRepository.increaseInterestRate(10.0);

        // Bulk updates bypass managed entities, so clear to force fresh DB reads.
        entityManager.clear();

        assertEquals(1, updatedRows);

        Loan refreshedPersonal = loanRepository.findById(personalActive.getLoanId()).orElseThrow();
        Loan refreshedHome = loanRepository.findById(homeActive.getLoanId()).orElseThrow();
        Loan refreshedClosed = loanRepository.findById(personalClosed.getLoanId()).orElseThrow();

        assertEquals(11.0, refreshedPersonal.getInterestRate(), 0.001);
        assertEquals(8.0, refreshedHome.getInterestRate(), 0.001);
        assertEquals(11.0, refreshedClosed.getInterestRate(), 0.001);
    }

    @Test
    void emiPaymentRepositoryFindOverduePendingPaymentsReturnsOnlyPendingPastDue() {
        Customer customer = createCustomer("Karan", "karan@example.com", "Delhi", 700);
        Loan loan = createLoan(LoanType.PERSONAL, LoanStatus.ACTIVE, 1200.0, 10.0);
        addLoan(customer, loan);

        EmiPayment overduePending = createPayment(LocalDate.now().minusDays(2), PaymentStatus.PENDING, 0.0);
        EmiPayment futurePending = createPayment(LocalDate.now().plusDays(2), PaymentStatus.PENDING, 0.0);
        EmiPayment overduePaid = createPayment(LocalDate.now().minusDays(5), PaymentStatus.PAID, 1200.0);
        overduePaid.setPaymentDate(LocalDate.now().minusDays(1));

        addPayment(loan, overduePending);
        addPayment(loan, futurePending);
        addPayment(loan, overduePaid);

        customerRepository.save(customer);

        List<EmiPayment> result = emiPaymentRepository.findOverduePendingPayments(LocalDate.now());

        assertEquals(1, result.size());
        assertEquals(PaymentStatus.PENDING, result.get(0).getPaymentStatus());
        assertTrue(result.get(0).getDueDate().isBefore(LocalDate.now()));
    }

    @Test
    void loanAndPenaltyReportingQueriesReturnAggregatedData() {
        Customer mumbaiCustomer = createCustomer("A", "a@example.com", "Mumbai", 780);
        Loan mumbaiLoan = createLoan(LoanType.PERSONAL, LoanStatus.ACTIVE, 1000.0, 10.0);
        addLoan(mumbaiCustomer, mumbaiLoan);
        EmiPayment mumbaiPending = createPayment(LocalDate.now().minusDays(1), PaymentStatus.PENDING, 200.0);
        addPayment(mumbaiLoan, mumbaiPending);
        addPenalty(mumbaiPending, 300.0, LocalDate.now().minusDays(1));

        Customer puneCustomer = createCustomer("B", "b@example.com", "Pune", 680);
        Loan puneLoan = createLoan(LoanType.HOME, LoanStatus.DEFAULTED, 1500.0, 9.0);
        addLoan(puneCustomer, puneLoan);
        EmiPayment puneMissed = createPayment(LocalDate.now().minusDays(3), PaymentStatus.MISSED, 0.0);
        addPayment(puneLoan, puneMissed);
        Penalty latestPenalty = addPenalty(puneMissed, 600.0, LocalDate.now());

        customerRepository.saveAll(List.of(mumbaiCustomer, puneCustomer));

        List<CityOutstandingDTO> cityOutstanding = loanRepository.getOutstandingAmountCityWise();
        List<CustomerPendingEmiDTO> defaulters = customerRepository.findTopDefaulters(PageRequest.of(0, 5));
        List<Penalty> latestPenaltyPage = penaltyRepository.findLatestPenaltyRecords(PageRequest.of(0, 1));

        assertFalse(cityOutstanding.isEmpty());
        assertEquals("Pune", cityOutstanding.get(0).city());

        assertFalse(defaulters.isEmpty());
        assertEquals("B", defaulters.get(0).customerName());
        assertTrue(defaulters.get(0).totalPendingEmi() >= 1500.0);

        assertEquals(1, latestPenaltyPage.size());
        assertNotNull(latestPenalty.getPenaltyId());
        assertEquals(latestPenalty.getPenaltyId(), latestPenaltyPage.get(0).getPenaltyId());
    }

    private Customer createCustomer(String name, String email, String city, int creditScore) {
        return Customer.builder()
                .customerName(name)
                .email(email)
                .phoneNumber("9876543210")
                .password("password123")
                .city(city)
                .creditScore(creditScore)
                .role(CustomerRole.CUSTOMER)
                .loans(new ArrayList<>())
                .build();
    }

    private Loan createLoan(LoanType type, LoanStatus status, double emiAmount, double interestRate) {
        return Loan.builder()
                .loanType(type)
                .loanAmount(100000.0)
                .interestRate(interestRate)
                .tenureMonths(60)
                .emiAmount(emiAmount)
                .loanStatus(status)
                .emiPayments(new ArrayList<>())
                .build();
    }

    private EmiPayment createPayment(LocalDate dueDate, PaymentStatus status, double amountPaid) {
        return EmiPayment.builder()
                .dueDate(dueDate)
                .paymentStatus(status)
                .amountPaid(amountPaid)
                .penalties(new ArrayList<>())
                .build();
    }

    private Penalty addPenalty(EmiPayment payment, double amount, LocalDate date) {
        Penalty penalty = Penalty.builder()
                .payment(payment)
                .penaltyAmount(amount)
                .penaltyReason("Test penalty")
                .generatedDate(date)
                .build();
        payment.getPenalties().add(penalty);
        return penalty;
    }

    private void addLoan(Customer customer, Loan loan) {
        loan.setCustomer(customer);
        customer.getLoans().add(loan);
    }

    private void addPayment(Loan loan, EmiPayment payment) {
        payment.setLoan(loan);
        loan.getEmiPayments().add(payment);
    }
}



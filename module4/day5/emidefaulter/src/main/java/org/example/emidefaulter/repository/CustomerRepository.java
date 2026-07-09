package org.example.emidefaulter.repository;

import org.example.emidefaulter.dto.CustomerLoanSummaryDTO;
import org.example.emidefaulter.dto.CustomerPendingEmiDTO;
import org.example.emidefaulter.entity.Customer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findByCity(String city);

    List<Customer> findByCreditScoreLessThan(Integer score);

    Optional<Customer> findByEmail(String email);

    @Query("""
            select c
            from Customer c
            join c.loans l
            join l.emiPayments e
            where e.paymentStatus = org.example.emidefaulter.entity.PaymentStatus.MISSED
            group by c
            having count(e.paymentId) > :missedEmiThreshold
            """)
    List<Customer> findHighRiskCustomers(@Param("missedEmiThreshold") long missedEmiThreshold);

    @Query("""
            select c
            from Customer c
            join c.loans l
            group by c
            having count(distinct l.loanType) > 1
            """)
    List<Customer> findCustomersWithMultipleLoanTypes();

    @Query("""
            select new org.example.emidefaulter.dto.CustomerLoanSummaryDTO(
                c.customerName,
                c.city,
                count(distinct l.loanId),
                coalesce(sum(case when e.paymentStatus in (org.example.emidefaulter.entity.PaymentStatus.PENDING, org.example.emidefaulter.entity.PaymentStatus.MISSED) then 1 else 0 end), 0),
                coalesce(sum(p.penaltyAmount), 0)
            )
            from Customer c
            left join c.loans l
            left join l.emiPayments e
            left join e.penalties p
            group by c.customerId, c.customerName, c.city
            """)
    List<CustomerLoanSummaryDTO> getCustomerLoanSummary();

    @Query("""
            select new org.example.emidefaulter.dto.CustomerPendingEmiDTO(
                c.customerId,
                c.customerName,
                coalesce(sum(case
                    when e.paymentStatus in (org.example.emidefaulter.entity.PaymentStatus.PENDING, org.example.emidefaulter.entity.PaymentStatus.MISSED) then (l.emiAmount - coalesce(e.amountPaid, 0))
                    else 0
                end), 0)
            )
            from Customer c
            join c.loans l
            left join l.emiPayments e
            group by c.customerId, c.customerName
            order by coalesce(sum(case
                when e.paymentStatus in (org.example.emidefaulter.entity.PaymentStatus.PENDING, org.example.emidefaulter.entity.PaymentStatus.MISSED) then (l.emiAmount - coalesce(e.amountPaid, 0))
                else 0
            end), 0) desc
            """)
    List<CustomerPendingEmiDTO> findTopDefaulters(Pageable pageable);

    @Query("select coalesce(avg(c.creditScore), 0) from Customer c")
    Double findAverageCreditScore();
}


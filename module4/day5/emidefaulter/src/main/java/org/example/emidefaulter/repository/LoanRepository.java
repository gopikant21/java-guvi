package org.example.emidefaulter.repository;

import org.example.emidefaulter.dto.CityOutstandingDTO;
import org.example.emidefaulter.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    List<Loan> findByLoanStatus(String status);

    List<Loan> findByLoanAmountGreaterThan(Double amount);

    @Query("""
            select new org.example.emidefaulter.dto.CityOutstandingDTO(
                c.city,
                coalesce(sum(case
                    when e.paymentStatus in ('PENDING', 'MISSED') then (l.emiAmount - coalesce(e.amountPaid, 0))
                    else 0
                end), 0)
            )
            from Loan l
            join l.customer c
            left join l.emiPayments e
            group by c.city
            order by coalesce(sum(case
                when e.paymentStatus in ('PENDING', 'MISSED') then (l.emiAmount - coalesce(e.amountPaid, 0))
                else 0
            end), 0) desc
            """)
    List<CityOutstandingDTO> getOutstandingAmountCityWise();

    @Query("""
            select l
            from Loan l
            where not exists (
                select e
                from EmiPayment e
                where e.loan = l and e.paymentStatus = 'MISSED'
            )
            """)
    List<Loan> findLoansWithoutMissedEmi();

    @Modifying
    @Transactional
    @Query("""
            update Loan l
            set l.interestRate = l.interestRate + (l.interestRate * :percentage / 100.0)
            where l.loanStatus = 'ACTIVE' and lower(l.loanType) = 'personal'
            """)
    int increaseInterestRate(@Param("percentage") double percentage);

    @Query("""
            select distinct l
            from Loan l
            join fetch l.customer c
            where c.email = :email
            """)
    List<Loan> findLoansByCustomerEmail(@Param("email") String email);

    long countByLoanStatus(String loanStatus);

    @Query("""
            select c.city
            from Loan l
            join l.customer c
            where l.loanStatus = 'DEFAULTED'
            group by c.city
            order by count(l.loanId) desc
            """)
    List<String> findCityWithHighestDefaults(org.springframework.data.domain.Pageable pageable);
}


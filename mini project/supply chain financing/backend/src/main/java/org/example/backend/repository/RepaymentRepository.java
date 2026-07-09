package org.example.backend.repository;

import org.example.backend.entity.Loan;
import org.example.backend.entity.Repayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepaymentRepository extends JpaRepository<Repayment, Long> {
    List<Repayment> findByLoanOrderByPaymentDateDesc(Loan loan);
}


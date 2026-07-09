package org.example.backend.repository;

import org.example.backend.entity.AppUser;
import org.example.backend.entity.Loan;
import org.example.backend.entity.LoanStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByCustomerOrderByCreatedAtDesc(AppUser customer);

    List<Loan> findByStatus(LoanStatus status);

    List<Loan> findByCustomerId(Long customerId);

    List<Loan> findByStatusAndCustomerId(LoanStatus status, Long customerId);

    long countByStatus(LoanStatus status);
}


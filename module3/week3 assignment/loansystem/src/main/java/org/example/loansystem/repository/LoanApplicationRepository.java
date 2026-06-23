package org.example.loansystem.repository;

import org.example.loansystem.model.LoanApplication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanApplicationRepository extends JpaRepository<LoanApplication, String> {
}


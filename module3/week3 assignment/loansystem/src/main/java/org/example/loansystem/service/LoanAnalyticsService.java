package org.example.loansystem.service;

import org.example.loansystem.LendingAnalytics;
import org.example.loansystem.model.LoanApplication;
import org.example.loansystem.repository.LoanApplicationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class LoanAnalyticsService {

    private final LoanApplicationRepository loanApplicationRepository;

    public LoanAnalyticsService(LoanApplicationRepository loanApplicationRepository) {
        this.loanApplicationRepository = loanApplicationRepository;
    }

    @Transactional
    public long loadApplications(List<String> records) {
        LendingAnalytics analytics = buildAnalytics();
        analytics.loadApplications(records);

        Collection<LoanApplication> merged = analytics.allApplications();
        loanApplicationRepository.deleteAllInBatch();
        loanApplicationRepository.saveAll(merged);
        return merged.size();
    }

    public List<LoanApplication> topCreditProfiles(int n) {
        return buildAnalytics().topCreditProfiles(n);
    }

    public Map<String, Double> averageLoanAmountByType() {
        return buildAnalytics().averageLoanAmountByType();
    }

    public Optional<LoanApplication> highestLoanApplication() {
        return buildAnalytics().highestLoanApplication();
    }

    public Set<String> lendersWithMultipleLoanTypes() {
        return buildAnalytics().lendersWithMultipleLoanTypes();
    }

    public Map<String, List<LoanApplication>> groupApplicationsByLender() {
        return buildAnalytics().groupApplicationsByLender();
    }

    public List<String> suspiciousApplications() {
        return buildAnalytics().suspiciousApplications();
    }

    public Map<String, Map<String, Optional<LoanApplication>>> loanTypeWiseTopApplicantByLender() {
        return buildAnalytics().loanTypeWiseTopApplicantByLender();
    }

    private LendingAnalytics buildAnalytics() {
        LendingAnalytics analytics = new LendingAnalytics();
        analytics.loadExistingApplications(loanApplicationRepository.findAll());
        return analytics;
    }
}


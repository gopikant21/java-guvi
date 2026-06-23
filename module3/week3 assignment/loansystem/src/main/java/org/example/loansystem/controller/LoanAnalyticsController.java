package org.example.loansystem.controller;

import org.example.loansystem.model.LoanApplication;
import org.example.loansystem.service.LoanAnalyticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/loans")
public class LoanAnalyticsController {

    private final LoanAnalyticsService loanAnalyticsService;

    public LoanAnalyticsController(LoanAnalyticsService loanAnalyticsService) {
        this.loanAnalyticsService = loanAnalyticsService;
    }

    @PostMapping("/load")
    public ResponseEntity<Map<String, Object>> loadApplications(@RequestBody LoadApplicationsRequest request) {
        List<String> records = request == null || request.records() == null ? List.of() : request.records();
        long storedCount = loanAnalyticsService.loadApplications(records);
        return ResponseEntity.ok(Map.of(
                "message", "Applications loaded successfully",
                "storedCount", storedCount
        ));
    }

    @GetMapping("/top-credit-profiles")
    public List<LoanApplication> topCreditProfiles(@RequestParam(defaultValue = "10") int n) {
        return loanAnalyticsService.topCreditProfiles(n);
    }

    @GetMapping("/average-loan-amount-by-type")
    public Map<String, Double> averageLoanAmountByType() {
        return loanAnalyticsService.averageLoanAmountByType();
    }

    @GetMapping("/highest-loan-application")
    public ResponseEntity<LoanApplication> highestLoanApplication() {
        return loanAnalyticsService.highestLoanApplication()
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/lenders-with-multiple-loan-types")
    public Set<String> lendersWithMultipleLoanTypes() {
        return loanAnalyticsService.lendersWithMultipleLoanTypes();
    }

    @GetMapping("/group-applications-by-lender")
    public Map<String, List<LoanApplication>> groupApplicationsByLender() {
        return loanAnalyticsService.groupApplicationsByLender();
    }

    @GetMapping("/suspicious-applications")
    public List<String> suspiciousApplications() {
        return loanAnalyticsService.suspiciousApplications();
    }

    @GetMapping("/loan-type-wise-top-applicant-by-lender")
    public Map<String, Map<String, LoanApplication>> loanTypeWiseTopApplicantByLender() {
        Map<String, Map<String, Optional<LoanApplication>>> raw = loanAnalyticsService.loanTypeWiseTopApplicantByLender();

        // Unwrap Optional values for cleaner JSON responses.
        Map<String, Map<String, LoanApplication>> result = new LinkedHashMap<>();
        raw.forEach((loanType, byLender) -> {
            Map<String, LoanApplication> lenderMap = new LinkedHashMap<>();
            byLender.forEach((lender, maybeApp) -> lenderMap.put(lender, maybeApp.orElse(null)));
            result.put(loanType, lenderMap);
        });

        return result;
    }

    public record LoadApplicationsRequest(List<String> records) {
    }
}




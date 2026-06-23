package org.example.loansystem;

import org.example.loansystem.model.LoanApplication;

import java.util.*;

public class Main {

    public static void main(String[] args) {

        List<String> records = new ArrayList<>(List.of(
                "A101|Rahul Sharma|HDFC|Personal Loan|500000|780",
                "A102|Priya Verma|ICICI|Home Loan|4500000|820",
                "A103|Amit Singh|Axis Bank|Car Loan|900000|760",
                // duplicate of A101 -> higher credit score wins (keep this)
                "A101|Rahul Sharma|HDFC|Personal Loan|450000|800",
                // invalid records
                "A104||SBI|Personal Loan|100000|700",      // empty name
                "A105|John Doe|SBI|Personal Loan|-5|700",   // amount <= 0
                "A106|Jane Doe|SBI|Personal Loan|100000|250",// credit < 300
                "   ",                                        // blank
                // suspicious: consecutive repeated words (C1)
                "A107|Rahul Rahul Sharma|HDFC|Personal Loan|120000|650",
                // suspicious: lender inside name (C2)
                "A108|HDFC Mohan|HDFC|Personal Loan|110000|640",
                // suspicious: more than 3 words (C5)
                "A109|Ravi Kumar Singh Sharma|SBI|Car Loan|300000|700",
                // Condition 7: same type/amount/credit, different names
                "A110|Karan Mehta|ICICI|Gold Loan|250000|710",
                "A111|Sita Rao|ICICI|Gold Loan|250000|710",
                // Condition 8: anagram within same lender
                "A112|RAM KUMAR|Axis Bank|Personal Loan|130000|705",
                "A113|KUMAR RAM|Axis Bank|Personal Loan|140000|715"
        ));

        LendingAnalytics analytics = new LendingAnalytics();
        analytics.loadApplications(records);

        System.out.println("=== Top 3 Credit Profiles ===");
        analytics.topCreditProfiles(3).forEach(System.out::println);

        System.out.println("\n=== Average Loan Amount By Type ===");
        analytics.averageLoanAmountByType().forEach((k, v) -> System.out.println(k + " -> " + v));

        System.out.println("\n=== Highest Loan Application ===");
        System.out.println(analytics.highestLoanApplication().orElse(null));

        System.out.println("\n=== Lenders With Multiple Loan Types ===");
        System.out.println(analytics.lendersWithMultipleLoanTypes());

        System.out.println("\n=== Group Applications By Lender ===");
        analytics.groupApplicationsByLender().forEach((lender, apps) -> {
            System.out.println(lender);
            apps.forEach(a -> System.out.println("   " + a));
        });

        System.out.println("\n=== Suspicious Applications ===");
        System.out.println(analytics.suspiciousApplications());

        System.out.println("\n=== Loan-Type Wise Top Applicant By Lender ===");
        analytics.loanTypeWiseTopApplicantByLender().forEach((type, byLender) -> {
            System.out.println(type);
            byLender.forEach((lender, app) ->
                    System.out.println("   " + lender + " -> "
                            + app.map(LoanApplication::getCustomerName).orElse("N/A")));
        });
    }
}


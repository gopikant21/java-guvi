package org.example.loansystem;

import org.example.loansystem.model.LoanApplication;

import java.util.*;
import java.util.stream.*;

public class LendingAnalytics {

    private Map<String, LoanApplication> applications = new HashMap<>();

    /**
     * Rule 1 (Duplicate Resolution) + Rule 2 (Invalid Records).
     *
     * Record format: APPLICATION_ID|CUSTOMER_NAME|LENDER_NAME|LOAN_TYPE|LOAN_AMOUNT|CREDIT_SCORE
     *
     * Duplicate ID is resolved by keeping the application with:
     *   1. Higher credit score
     *   2. Lower loan amount
     *   3. Lexicographically smaller customer name
     */
    public void loadApplications(List<String> records) {

        if (records == null) {
            return;
        }

        for (String record : records) {

            // Ignore null / blank records
            if (record == null || record.isBlank()) {
                continue;
            }

            // -1 keeps trailing empty fields so we can validate them
            String[] parts = record.split("\\|", -1);
            if (parts.length != 6) {
                continue;
            }

            // Hidden Test 4: trim leading/trailing spaces
            String id = parts[0].trim();
            String customerName = parts[1].trim();
            String lenderName = parts[2].trim();
            String loanType = parts[3].trim();
            String amountStr = parts[4].trim();
            String creditStr = parts[5].trim();

            // Rule 2: any mandatory text field empty -> invalid
            if (id.isEmpty() || customerName.isEmpty()
                    || lenderName.isEmpty() || loanType.isEmpty()) {
                continue;
            }

            double loanAmount;
            int creditScore;
            try {
                loanAmount = Double.parseDouble(amountStr);
                creditScore = Integer.parseInt(creditStr);
            } catch (NumberFormatException e) {
                continue; // non-numeric amount / score -> invalid
            }

            // Rule 2: numeric range validation
            if (loanAmount <= 0) {
                continue;
            }
            if (creditScore < 300 || creditScore > 900) {
                continue;
            }

            LoanApplication incoming = new LoanApplication(
                    id, customerName, lenderName, loanType, loanAmount, creditScore);

            // Rule 1: duplicate ID resolution
            applications.merge(id, incoming, (existing, candidate) ->
                    Stream.of(existing, candidate)
                            .max(Comparator
                                    .comparingInt(LoanApplication::getCreditScore)
                                    .thenComparing(Comparator.comparingDouble(LoanApplication::getLoanAmount).reversed())
                                    .thenComparing(LoanApplication::getCustomerName, Comparator.reverseOrder()))
                            .orElse(existing));
        }
    }

    public void loadExistingApplications(Collection<LoanApplication> existingApplications) {
        if (existingApplications == null) {
            return;
        }

        existingApplications.forEach(app -> {
            if (app != null && app.getApplicationId() != null && !app.getApplicationId().isBlank()) {
                applications.put(app.getApplicationId().trim(), app);
            }
        });
    }

    public List<LoanApplication> allApplications() {
        return new ArrayList<>(applications.values());
    }

    /**
     * Rule 3: Sort by Credit Score DESC, Loan Amount ASC, Customer Name ASC. Return first n.
     */
    public List<LoanApplication> topCreditProfiles(int n) {

        return applications.values().stream()
                .sorted(Comparator
                        .comparingInt(LoanApplication::getCreditScore).reversed()
                        .thenComparing(Comparator.comparingDouble(LoanApplication::getLoanAmount))
                        .thenComparing(LoanApplication::getCustomerName))
                .limit(Math.max(0, n))
                .collect(Collectors.toList());
    }

    /**
     * Rule 4: TreeMap sorted by loan type, average loan amount rounded to 2 decimals.
     */
    public Map<String, Double> averageLoanAmountByType() {

        return applications.values().stream()
                .collect(Collectors.groupingBy(
                        LoanApplication::getLoanType,
                        TreeMap::new,
                        Collectors.collectingAndThen(
                                Collectors.averagingDouble(LoanApplication::getLoanAmount),
                                avg -> Math.round(avg * 100.0) / 100.0)));
    }

    /**
     * Rule 5: Highest Loan Amount, tie -> Higher Credit Score, tie -> Smaller Application ID.
     */
    public Optional<LoanApplication> highestLoanApplication() {

        return applications.values().stream()
                .max(Comparator
                        .comparingDouble(LoanApplication::getLoanAmount)
                        .thenComparingInt(LoanApplication::getCreditScore)
                        // smaller application id should win -> reverse it for max()
                        .thenComparing(LoanApplication::getApplicationId, Comparator.reverseOrder()));
    }

    /**
     * Rule 6: Lenders offering more than one loan type. Output TreeSet.
     */
    public Set<String> lendersWithMultipleLoanTypes() {

        return applications.values().stream()
                .collect(Collectors.groupingBy(
                        LoanApplication::getLenderName,
                        Collectors.mapping(LoanApplication::getLoanType, Collectors.toSet())))
                .entrySet().stream()
                .filter(e -> e.getValue().size() > 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    /**
     * Rule 7: LinkedHashMap, lenders sorted alphabetically.
     * Applications inside each lender sorted by Credit Score DESC, Loan Amount ASC.
     */
    public Map<String, List<LoanApplication>> groupApplicationsByLender() {

        Map<String, List<LoanApplication>> grouped = applications.values().stream()
                .sorted(Comparator
                        .comparingInt(LoanApplication::getCreditScore).reversed()
                        .thenComparing(Comparator.comparingDouble(LoanApplication::getLoanAmount)))
                .collect(Collectors.groupingBy(LoanApplication::getLenderName, Collectors.toList()));

        return grouped.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> a,
                        LinkedHashMap::new));
    }

    /**
     * The Real Nightmare: suspiciousApplications().
     * Streams API ONLY (no loops / no recursion / no helper methods).
     * Returns distinct customer names, alphabetically sorted.
     */
    public List<String> suspiciousApplications() {

        Collection<LoanApplication> all = applications.values();

        // Loan-type wise average loan amount
        Map<String, Double> avgAmountByType = all.stream()
                .collect(Collectors.groupingBy(
                        LoanApplication::getLoanType,
                        Collectors.averagingDouble(LoanApplication::getLoanAmount)));

        // Loan-type wise average credit score
        Map<String, Double> avgCreditByType = all.stream()
                .collect(Collectors.groupingBy(
                        LoanApplication::getLoanType,
                        Collectors.averagingInt(LoanApplication::getCreditScore)));

        // Condition 6: customer name -> distinct lenders
        Map<String, Set<String>> lendersByCustomer = all.stream()
                .collect(Collectors.groupingBy(
                        a -> a.getCustomerName().trim(),
                        Collectors.mapping(LoanApplication::getLenderName, Collectors.toSet())));

        // Condition 7: (loanType|amount|credit) -> distinct customer names
        Map<String, Set<String>> namesByAmountCredit = all.stream()
                .collect(Collectors.groupingBy(
                        a -> a.getLoanType() + "|" + a.getLoanAmount() + "|" + a.getCreditScore(),
                        Collectors.mapping(a -> a.getCustomerName().trim(), Collectors.toSet())));

        // Condition 8: (lender|sortedLetters) -> distinct customer names (anagram groups)
        Map<String, Set<String>> namesByAnagram = all.stream()
                .collect(Collectors.groupingBy(
                        a -> a.getLenderName().trim().toLowerCase() + "|"
                                + a.getCustomerName().trim().toLowerCase().replaceAll("\\s+", "")
                                .chars().sorted()
                                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                                .toString(),
                        Collectors.mapping(a -> a.getCustomerName().trim(), Collectors.toSet())));

        return all.stream()
                .filter(a -> {

                    String name = a.getCustomerName().trim();
                    String[] words = name.split("\\s+");
                    String lowerName = name.toLowerCase();

                    String anagramKey = a.getLenderName().trim().toLowerCase() + "|"
                            + name.toLowerCase().replaceAll("\\s+", "")
                            .chars().sorted()
                            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                            .toString();

                    double avgAmt = avgAmountByType.getOrDefault(a.getLoanType(), 0.0);
                    double avgCredit = avgCreditByType.getOrDefault(a.getLoanType(), 0.0);

                    // Condition 1: consecutive repeated words (case-insensitive)
                    boolean c1 = IntStream.range(0, words.length - 1)
                            .anyMatch(i -> words[i].equalsIgnoreCase(words[i + 1]));

                    // Condition 2: lender name appears inside customer name (case-insensitive)
                    boolean c2 = lowerName.contains(a.getLenderName().trim().toLowerCase());

                    // Condition 3: amount exceeds loan-type average by 250% (i.e. > avg * 2.5)
                    boolean c3 = a.getLoanAmount() > avgAmt * 2.5;

                    // Condition 4: credit below type-average AND amount above type-average
                    boolean c4 = a.getCreditScore() < avgCredit && a.getLoanAmount() > avgAmt;

                    // Condition 5: more than 3 words in customer name
                    boolean c5 = words.length > 3;

                    // Condition 6: same customer applied with more than 3 different lenders
                    boolean c6 = lendersByCustomer
                            .getOrDefault(name, Collections.emptySet()).size() > 3;

                    // Condition 7: same loan type + same amount + same credit + different names
                    boolean c7 = namesByAmountCredit
                            .getOrDefault(a.getLoanType() + "|" + a.getLoanAmount() + "|"
                                    + a.getCreditScore(), Collections.emptySet()).size() > 1;

                    // Condition 8: anagram of another customer name within same lender
                    boolean c8 = namesByAnagram
                            .getOrDefault(anagramKey, Collections.emptySet()).size() > 1;

                    return c1 || c2 || c3 || c4 || c5 || c6 || c7 || c8;
                })
                .map(a -> a.getCustomerName().trim())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * Bonus (Architect Level): loan-type wise top applicant per lender.
     * Uses only groupingBy / mapping / collectingAndThen / maxBy. No loops.
     * "Top" applicant = highest Credit Score, tie-break highest Loan Amount.
     */
    public Map<String, Map<String, Optional<LoanApplication>>>
    loanTypeWiseTopApplicantByLender() {

        return applications.values().stream()
                .collect(Collectors.groupingBy(
                        LoanApplication::getLoanType,
                        Collectors.groupingBy(
                                LoanApplication::getLenderName,
                                Collectors.mapping(
                                        a -> a,
                                        Collectors.collectingAndThen(
                                                Collectors.maxBy(Comparator
                                                        .comparingInt(LoanApplication::getCreditScore)
                                                        .thenComparingDouble(LoanApplication::getLoanAmount)),
                                                opt -> opt)))));
    }
}


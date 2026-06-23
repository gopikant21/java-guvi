package org.example.loansystem.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "loan_applications")
public class LoanApplication {

    @Id
    @Column(name = "application_id", nullable = false, updatable = false)
    private String applicationId;

    @Column(name = "customer_name", nullable = false)
    private String customerName;

    @Column(name = "lender_name", nullable = false)
    private String lenderName;

    @Column(name = "loan_type", nullable = false)
    private String loanType;

    @Column(name = "loan_amount", nullable = false)
    private double loanAmount;

    @Column(name = "credit_score", nullable = false)
    private int creditScore;

    protected LoanApplication() {
        // JPA requires a no-arg constructor.
    }

    public LoanApplication(String applicationId,
                           String customerName,
                           String lenderName,
                           String loanType,
                           double loanAmount,
                           int creditScore) {

        this.applicationId = applicationId;
        this.customerName = customerName;
        this.lenderName = lenderName;
        this.loanType = loanType;
        this.loanAmount = loanAmount;
        this.creditScore = creditScore;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getLenderName() {
        return lenderName;
    }

    public void setLenderName(String lenderName) {
        this.lenderName = lenderName;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public double getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(double loanAmount) {
        this.loanAmount = loanAmount;
    }

    public int getCreditScore() {
        return creditScore;
    }

    public void setCreditScore(int creditScore) {
        this.creditScore = creditScore;
    }

    @Override
    public String toString() {
        return applicationId + "|" + customerName + "|" + lenderName + "|"
                + loanType + "|" + loanAmount + "|" + creditScore;
    }
}


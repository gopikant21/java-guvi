package day2.streamDemo.entity;

public class Loan {
    int loanId;
    int loanAmount;
    int loanTenure;
    int loanInterestRate;
    String loanStatus;
    String loanType;

    public Loan(int loanId, int loanAmount, int loanTenure, int loanInterestRate,  String loanStatus, String loanType) {
        this.loanId = loanId;
        this.loanAmount = loanAmount;
        this.loanTenure = loanTenure;
        this.loanInterestRate = loanInterestRate;
        this.loanStatus = loanStatus;
        this.loanType = loanType;
    }

    public int getLoanId() {
        return loanId;
    }

    public void setLoanId(int loanId) {
        this.loanId = loanId;
    }

    public int getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(int loanAmount) {
        this.loanAmount = loanAmount;
    }

    public int getLoanTenure() {
        return loanTenure;
    }

    public void setLoanTenure(int loanTenure) {
        this.loanTenure = loanTenure;
    }

    public int getLoanInterestRate() {
        return loanInterestRate;
    }

    public void setLoanInterestRate(int loanInterestRate) {
        this.loanInterestRate = loanInterestRate;
    }

    public String getLoanStatus() {
        return loanStatus;
    }

    public void setLoanStatus(String loanStatus) {
        this.loanStatus = loanStatus;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    @Override
    public String toString() {
        return "Loan[id=" + loanId + ", loanAmount=" + loanAmount + ", loanTenure=" + loanTenure + ", loanInterestRate=" + loanInterestRate + ", loanStatus=" + loanStatus + ", loanType=" + loanType + "]\n";
    }
}

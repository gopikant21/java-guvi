package encapsulation;

public class LoanAccount {

    // Private fields (data hiding)
    private String customerId;
    private double loanAmount;
    private double interestRate;
    private int tenureMonths;
    private double outstandingBalance;

    // Constructor
    public LoanAccount(String customerId, double loanAmount, double interestRate, int tenureMonths) {
        this.customerId = customerId;
        this.loanAmount = loanAmount;
        this.interestRate = interestRate;
        this.tenureMonths = tenureMonths;
        this.outstandingBalance = loanAmount;
    }

    // Getter methods
    public String getCustomerId() {
        return customerId;
    }

    public double getLoanAmount() {
        return loanAmount;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public int getTenureMonths() {
        return tenureMonths;
    }

    public double getOutstandingBalance() {
        return outstandingBalance;
    }

    // Setter methods with validation (important in fintech)
    public void setInterestRate(double interestRate) {
        if (interestRate > 0 && interestRate < 50) {
            this.interestRate = interestRate;
        } else {
            System.out.println("Invalid interest rate!");
        }
    }

    public void setTenureMonths(int tenureMonths) {
        if (tenureMonths > 0 && tenureMonths <= 360) {
            this.tenureMonths = tenureMonths;
        } else {
            System.out.println("Invalid tenure!");
        }
    }

    // Business method: calculate EMI
    public double calculateEMI() {
        double monthlyRate = interestRate / (12 * 100);
        double emi = (loanAmount * monthlyRate * Math.pow(1 + monthlyRate, tenureMonths))
                / (Math.pow(1 + monthlyRate, tenureMonths) - 1);
        return emi;
    }

    // Business method: make repayment
    public void makeRepayment(double amount) {
        if (amount > 0 && amount <= outstandingBalance) {
            outstandingBalance -= amount;
            System.out.println("Repayment successful. Remaining balance: " + outstandingBalance);
        } else {
            System.out.println("Invalid repayment amount!");
        }
    }
}
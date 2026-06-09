package inheritance;

public class Loan {

    protected String customerId;
    protected double principal;
    protected double interestRate;
    protected int tenureMonths;

    public Loan(String customerId, double principal, double interestRate, int tenureMonths) {
        this.customerId = customerId;
        this.principal = principal;
        this.interestRate = interestRate;
        this.tenureMonths = tenureMonths;
    }

    public double calculateEMI() {
        double r = interestRate / (12 * 100);
        return (principal * r * Math.pow(1 + r, tenureMonths)) /
                (Math.pow(1 + r, tenureMonths) - 1);
    }

    public void displayLoanDetails() {
        System.out.println("Customer ID: " + customerId);
        System.out.println("Principal: " + principal);
        System.out.println("Interest Rate: " + interestRate);
        System.out.println("Tenure (months): " + tenureMonths);
    }
}
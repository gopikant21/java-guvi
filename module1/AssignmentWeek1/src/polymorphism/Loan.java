package polymorphism;

public class Loan {

    protected String customerId;
    protected double principal;

    public Loan(String customerId, double principal) {
        this.customerId = customerId;
        this.principal = principal;
    }

    // Common method (to be overridden)
    public double calculateInterest() {
        return 0;
    }
}
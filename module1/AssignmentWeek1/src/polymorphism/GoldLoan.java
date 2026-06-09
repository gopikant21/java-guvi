package polymorphism;

public class GoldLoan extends Loan {

    public GoldLoan(String customerId, double principal) {
        super(customerId, principal);
    }

    @Override
    public double calculateInterest() {
        // Medium interest
        return principal * 0.12;
    }
}
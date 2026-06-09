package abstraction;

public class HomeLoan extends Loan {

    public HomeLoan(String customerId, double principal) {
        super(customerId, principal);
    }

    @Override
    public double calculateInterest() {
        return principal * 0.08; // 8% interest
    }
}

package abstraction;

public class GoldLoan extends Loan {

    public GoldLoan(String customerId, double principal) {
        super(customerId, principal);
    }

    @Override
    public double calculateInterest() {
        return principal * 0.12; // 12% interest
    }
}

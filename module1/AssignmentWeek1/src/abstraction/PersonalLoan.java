package abstraction;

public class PersonalLoan extends Loan {

    public PersonalLoan(String customerId, double principal) {
        super(customerId, principal);
    }

    @Override
    public double calculateInterest() {
        return principal * 0.15; // 15% interest
    }
}
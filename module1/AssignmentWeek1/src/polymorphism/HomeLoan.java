package polymorphism;

public class HomeLoan extends Loan {

    public HomeLoan(String customerId, double principal) {
        super(customerId, principal);
    }

    @Override
    public double calculateInterest() {
        // Lower interest for secured loan
        return principal * 0.08;
    }
}
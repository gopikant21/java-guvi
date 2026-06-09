package polymorphism;

public class PersonalLoan extends Loan {

    public PersonalLoan(String customerId, double principal) {
        super(customerId, principal);
    }

    @Override
    public double calculateInterest() {
        // Higher interest for unsecured loan
        return principal * 0.15;
    }
}
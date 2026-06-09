package inheritance;

public class PersonalLoan extends Loan {

    private double processingFee;

    public PersonalLoan(String customerId, double principal, double interestRate, int tenureMonths, double processingFee) {
        super(customerId, principal, interestRate, tenureMonths);
        this.processingFee = processingFee;
    }

    public void displayPersonalLoanDetails() {
        displayLoanDetails(); // inherited method
        System.out.println("Processing Fee: " + processingFee);
    }
}
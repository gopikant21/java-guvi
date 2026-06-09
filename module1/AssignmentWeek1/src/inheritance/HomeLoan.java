package inheritance;

public class HomeLoan extends Loan {

    private double propertyValue;

    public HomeLoan(String customerId, double principal, double interestRate, int tenureMonths, double propertyValue) {
        super(customerId, principal, interestRate, tenureMonths);
        this.propertyValue = propertyValue;
    }

    // Method overriding (polymorphism + inheritance)
    @Override
    public double calculateEMI() {
        double baseEMI = super.calculateEMI();

        // Example: subsidy benefit for home loans
        return baseEMI - 500;
    }

    public void displayHomeLoanDetails() {
        displayLoanDetails();
        System.out.println("Property Value: " + propertyValue);
    }
}
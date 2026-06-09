package abstraction;

public abstract class Loan {

    protected String customerId;
    protected double principal;

    public Loan(String customerId, double principal) {
        this.customerId = customerId;
        this.principal = principal;
    }

    // Abstract method (must be implemented by child classes)
    public abstract double calculateInterest();

    // Concrete method
    public void displayDetails() {
        System.out.println("Customer ID: " + customerId);
        System.out.println("Principal Amount: " + principal);
    }
}

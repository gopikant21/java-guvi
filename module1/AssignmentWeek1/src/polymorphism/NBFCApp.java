package polymorphism;

public class NBFCApp {

    public static void main(String[] args) {

        // Parent reference, child objects
        Loan loan1 = new PersonalLoan("C001", 100000);
        Loan loan2 = new HomeLoan("C002", 500000);
        Loan loan3 = new GoldLoan("C003", 200000);

        // Same method, different behavior
        System.out.println("Personal Loan Interest: " + loan1.calculateInterest());
        System.out.println("Home Loan Interest: " + loan2.calculateInterest());
        System.out.println("Gold Loan Interest: " + loan3.calculateInterest());
    }
}

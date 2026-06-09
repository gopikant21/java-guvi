package abstraction;

public class NBFCApp {

    public static void main(String[] args) {

        Loan loan1 = new PersonalLoan("C001", 100000);
        Loan loan2 = new HomeLoan("C002", 500000);
        Loan loan3 = new GoldLoan("C003", 200000);

        loan1.displayDetails();
        System.out.println("Interest: " + loan1.calculateInterest());

        System.out.println();

        loan2.displayDetails();
        System.out.println("Interest: " + loan2.calculateInterest());

        System.out.println();

        loan3.displayDetails();
        System.out.println("Interest: " + loan3.calculateInterest());
    }
}

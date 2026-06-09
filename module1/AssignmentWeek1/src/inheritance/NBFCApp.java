package inheritance;

public class NBFCApp {

    public static void main(String[] args) {

        PersonalLoan pLoan = new PersonalLoan("C001", 300000, 13.5, 36, 2000);
        System.out.println("=== Personal Loan ===");
        pLoan.displayPersonalLoanDetails();
        System.out.println("EMI: " + pLoan.calculateEMI());

        System.out.println();

        HomeLoan hLoan = new HomeLoan("C002", 2000000, 9.0, 120, 2500000);
        System.out.println("=== Home Loan ===");
        hLoan.displayHomeLoanDetails();
        System.out.println("EMI (after subsidy): " + hLoan.calculateEMI());
    }
}
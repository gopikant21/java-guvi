package encapsulation;

public class NBFCApp {
    public static void main(String[] args) {

        LoanAccount loan = new LoanAccount("CUST123", 500000, 12.5, 60);

        System.out.println("Customer: " + loan.getCustomerId());
        System.out.println("EMI: " + loan.calculateEMI());

        loan.makeRepayment(10000);

        // Trying to set invalid interest rate
        loan.setInterestRate(70);  // Will fail validation
    }
}
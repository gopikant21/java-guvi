package day2.streamDemo;

import day2.streamDemo.Dao.LoanDAO;
import day2.streamDemo.entity.Loan;
import day2.streamDemo.ui.LoanDaoImp;


public class LoanMainStream {
    public static void main(String[] args) {
        LoanDAO loanList = new LoanDaoImp();

        loanList.add(new Loan(1, 500000, 60, 10, "Approved", "Home"));
        loanList.add(new Loan(2, 200000, 36, 12, "Pending", "Personal"));
        loanList.add(new Loan(3, 800000, 120, 9, "Approved", "Home"));
        loanList.add(new Loan(4, 150000, 24, 14, "Rejected", "Personal"));
        loanList.add(new Loan(5, 300000, 48, 11, "Approved", "Car"));
        loanList.add(new Loan(6, 1000000, 180, 8, "Pending", "Home"));
        loanList.add(new Loan(7, 250000, 36, 13, "Approved", "Car"));
        loanList.add(new Loan(8, 400000, 72, 10, "Rejected", "Business"));
        loanList.add(new Loan(9, 600000, 84, 9, "Approved", "Business"));
        loanList.add(new Loan(10, 120000, 12, 15, "Pending", "Personal"));

        System.out.println("Rejected Loans: ");
        System.out.println(loanList.getLoanByStatus("Pending"));

        System.out.println("\nApproved Loans: ");
        System.out.println(loanList.getLoanByStatus("Approved"));

        System.out.println("\nRejected Loans: ");
        System.out.println(loanList.getLoanByStatus("Rejected"));

        System.out.println("\nIncrease interest rate: ");
        System.out.println(loanList.increaseInterestRate(5));

        System.out.println("\nMax loan amount: ");
        System.out.println(loanList.maxLoanAmount());

        System.out.println("\nMin loan amount: ");
        System.out.println(loanList.minLoanAmount());

        System.out.println("\nTotal loan amount: ");
        System.out.println(loanList.totalLoanAmount());

        System.out.println("\nDisplaying all loans");
        loanList.displayAllLoan();

        System.out.println("\n collect practice: ");
        loanList.collectPractice();

    }
}

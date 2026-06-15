package day2.streamDemo.Dao;

import day2.streamDemo.entity.Loan;

public interface LoanDAO {
    public void add(Loan loan);
    public Iterable<Loan> getLoanByStatus(String status);
    public Iterable<Loan> gteLoanByTenure(int loanTenure);
    public Iterable<Loan> gteLoanByInterestRate(int loanInterestRate);
    public Iterable<Loan> gteLoanByLoanType(String loanType);
    public Iterable<Loan> getLoanByAmount(int loanAmount);
    public Iterable<Loan> increaseInterestRate(int loanIncreaseRate);
    public Loan maxLoanAmount();
    public Loan minLoanAmount();
    public int totalLoanAmount();
    public void displayAllLoan();
    public void collectPractice();


}

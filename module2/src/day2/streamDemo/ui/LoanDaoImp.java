package day2.streamDemo.ui;

import day2.streamDemo.Dao.LoanDAO;
import day2.streamDemo.entity.Loan;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class LoanDaoImp implements LoanDAO {

    private List<Loan> loans = new ArrayList<>();

    @Override
    public void add(Loan loan) {
        loans.add(loan);
    }

    @Override
    public List<Loan> getLoanByStatus(String status) {
        return loans.stream().filter((Loan loan)->loan.getLoanStatus().equals(status)).toList();
    }

    @Override
    public Iterable<Loan> gteLoanByTenure(int loanTenure) {
        return loans.stream().filter((Loan loan)->loan.getLoanTenure()==loanTenure).toList();
    }

    @Override
    public Iterable<Loan> gteLoanByInterestRate(int loanInterestRate) {
        return loans.stream().filter((Loan loan)->loan.getLoanInterestRate()==loanInterestRate).toList();
    }

    @Override
    public Iterable<Loan> gteLoanByLoanType(String loanType) {
        return loans.stream().filter((Loan loan)->loan.getLoanType().equals(loanType)).toList();
    }

    @Override
    public List<Loan> getLoanByAmount(int loanAmount) {
        return loans.stream().filter((Loan loan)->loan.getLoanAmount()==loanAmount).toList();
    }

    @Override
    public List<Loan> increaseInterestRate(int loanIncreaseRate) {
        return loans.stream()
                .filter((Loan loan)->loan.getLoanType().equals("Personal"))
                .map((loan)->{
                    loan.setLoanInterestRate(loan.getLoanInterestRate()+loanIncreaseRate);
                    return loan;
                })
                .sorted(Comparator.comparing(Loan::getLoanAmount))
                .toList();
    }

    @Override
    public Loan maxLoanAmount() {
        return loans.stream().max(Comparator.comparing(Loan::getLoanAmount)).get();
    }

    @Override
    public Loan minLoanAmount() {
        return loans.stream().min(Comparator.comparing(Loan::getLoanAmount)).get();
    }

    @Override
    public int totalLoanAmount() {
        return loans.stream().mapToInt(Loan::getLoanAmount).reduce(0, (a,b)->a+b);
    }

    @Override
    public void displayAllLoan() {
        loans.stream().forEach(System.out::println);
    }

    @Override
    public void collectPractice() {
        System.out.println(
                loans.stream()
                        //.collect(Collectors.averagingInt(Loan::getLoanAmount))
                        //.collect(Collectors.groupingBy(Loan::getLoanType, Collectors.counting()))
                        //.collect(Collectors.groupingBy(Loan::getLoanType, Collectors.summingInt(Loan::getLoanAmount)))
                        .collect(Collectors.groupingBy(Loan::getLoanType))
        );
    }
}
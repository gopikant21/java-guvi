package day4.jdbcDAO.dao;

import java.util.List;
import day4.jdbcDAO.entity.Loan;

public interface LoanDAO {

    void addLoan(Loan loan);

    Loan getLoanById(int id);

    List<Loan> getAllLoans();

    void updateLoan(Loan loan);

    void deleteLoan(int id);

    List<Loan> getLoansByBorrowerName(String borrowerName);

    List<Loan> getLoansByAmount(double amount);

    int countLoans();

    List<Loan> sortByBorrowerName();

    boolean exists(int id);

    void deleteAll();
}
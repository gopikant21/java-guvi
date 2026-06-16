package day4.jdbcDAO;

import java.sql.Connection;
import java.util.List;

import day4.jdbcDAO.dao.LoanDAO;
import day4.jdbcDAO.ui.LoanDAOimpl;
import day4.jdbcDAO.entity.Loan;
import day4.jdbcDAO.connection.DBManager;

public class LoanMain {
    public static void main(String[] args) {

        Connection conn = null;

        try {
            conn = DBManager.getConnection();

            LoanDAO dao = new LoanDAOimpl(conn);

            Loan l1 = new Loan(0, "Gopi", 50000, 8.5, 12);
            dao.addLoan(l1);

            Loan l2 = new Loan(0, "Ravi", 100000, 7.2, 24);
            dao.addLoan(l2);

            Loan loan = dao.getLoanById(1);
            System.out.println("By ID: " + loan);

            List<Loan> all = dao.getAllLoans();
            System.out.println("All Loans:");
            for (Loan l : all) {
                System.out.println(l);
            }

            if (loan != null) {
                loan.setAmount(60000);
                dao.updateLoan(loan);
            }

            List<Loan> byBorrower = dao.getLoansByBorrowerName("Gopi");
            System.out.println("Loans by Borrower:");
            for (Loan l : byBorrower) {
                System.out.println(l);
            }

            System.out.println("Count: " + dao.countLoans());

            List<Loan> sorted = dao.sortByBorrowerName();
            System.out.println("Sorted:");
            for (Loan l : sorted) {
                System.out.println(l);
            }

            System.out.println("Exists ID 1: " + dao.exists(1));

            dao.deleteLoan(2);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager.closeConnection(conn);
        }
    }
}

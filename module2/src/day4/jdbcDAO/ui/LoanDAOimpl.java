package day4.jdbcDAO.ui;

import java.sql.*;
        import java.util.*;
        import day4.jdbcDAO.dao.LoanDAO;
import day4.jdbcDAO.entity.Loan;

public class LoanDAOimpl implements LoanDAO {

    private Connection conn;

    public LoanDAOimpl(Connection conn) {
        this.conn = conn;
    }

    public void addLoan(Loan loan) {
        try {
            String sql = "INSERT INTO loan(borrowerName, amount, interestRate, durationMonths) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, loan.getBorrowerName());
            ps.setDouble(2, loan.getAmount());
            ps.setDouble(3, loan.getInterestRate());
            ps.setInt(4, loan.getDurationMonths());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Loan getLoanById(int id) {
        try {
            String sql = "SELECT * FROM loan WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Loan(
                        rs.getInt("id"),
                        rs.getString("borrowerName"),
                        rs.getDouble("amount"),
                        rs.getDouble("interestRate"),
                        rs.getInt("durationMonths")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Loan> getAllLoans() {
        List<Loan> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM loan";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                list.add(new Loan(
                        rs.getInt("id"),
                        rs.getString("borrowerName"),
                        rs.getDouble("amount"),
                        rs.getDouble("interestRate"),
                        rs.getInt("durationMonths")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public void updateLoan(Loan loan) {
        try {
            String sql = "UPDATE loan SET borrowerName=?, amount=?, interestRate=?, durationMonths=? WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, loan.getBorrowerName());
            ps.setDouble(2, loan.getAmount());
            ps.setDouble(3, loan.getInterestRate());
            ps.setInt(4, loan.getDurationMonths());
            ps.setInt(5, loan.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteLoan(int id) {
        try {
            String sql = "DELETE FROM loan WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Loan> getLoansByBorrowerName(String borrowerName) {
        List<Loan> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM loan WHERE borrowerName=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, borrowerName);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Loan(
                        rs.getInt("id"),
                        rs.getString("borrowerName"),
                        rs.getDouble("amount"),
                        rs.getDouble("interestRate"),
                        rs.getInt("durationMonths")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Loan> getLoansByAmount(double amount) {
        List<Loan> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM loan WHERE amount=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDouble(1, amount);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Loan(
                        rs.getInt("id"),
                        rs.getString("borrowerName"),
                        rs.getDouble("amount"),
                        rs.getDouble("interestRate"),
                        rs.getInt("durationMonths")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public int countLoans() {
        try {
            String sql = "SELECT COUNT(*) FROM loan";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Loan> sortByBorrowerName() {
        List<Loan> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM loan ORDER BY borrowerName";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                list.add(new Loan(
                        rs.getInt("id"),
                        rs.getString("borrowerName"),
                        rs.getDouble("amount"),
                        rs.getDouble("interestRate"),
                        rs.getInt("durationMonths")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean exists(int id) {
        try {
            String sql = "SELECT 1 FROM loan WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void deleteAll() {
        try {
            String sql = "DELETE FROM loan";
            Statement st = conn.createStatement();
            st.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

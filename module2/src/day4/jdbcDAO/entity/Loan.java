package day4.jdbcDAO.entity;

public class Loan {

    private int id;
    private String borrowerName;
    private double amount;
    private double interestRate;
    private int durationMonths;

    public Loan() {
    }

    public Loan(int id, String borrowerName, double amount, double interestRate, int durationMonths) {
        this.id = id;
        this.borrowerName = borrowerName;
        this.amount = amount;
        this.interestRate = interestRate;
        this.durationMonths = durationMonths;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public void setBorrowerName(String borrowerName) {
        this.borrowerName = borrowerName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public int getDurationMonths() {
        return durationMonths;
    }

    public void setDurationMonths(int durationMonths) {
        this.durationMonths = durationMonths;
    }

    @Override
    public String toString() {
        return "Loan{" +
                "id=" + id +
                ", borrowerName='" + borrowerName + '\'' +
                ", amount=" + amount +
                ", interestRate=" + interestRate +
                ", durationMonths=" + durationMonths +
                '}';
    }
}
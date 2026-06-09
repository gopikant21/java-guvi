package association;

// Customer class
class Customer {
    String name;

    public Customer(String name) {
        this.name = name;
    }
}

// Loan class
class Loan {
    String loanId;

    public Loan(String loanId) {
        this.loanId = loanId;
    }

    // Association method
    public void assignLoan(Customer customer) {
        System.out.println(customer.name + " has taken loan: " + loanId);
    }
}

// Main
public class AssociationDemo {
    public static void main(String[] args) {
        Customer c = new Customer("Gopi");
        Loan loan = new Loan("LN001");

        loan.assignLoan(c);
    }
}

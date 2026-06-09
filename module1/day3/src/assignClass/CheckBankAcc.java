package assignClass;

public class CheckBankAcc {
    String accOwner;
    double bal;
    public CheckBankAcc(String accOwner, double bal) {
        this.accOwner = accOwner;
        this.bal = bal;
    }

    public String getAccOwner() {
        return accOwner;
    }
    public void setAccOwner(String accOwner) {
        this.accOwner = accOwner;
    }
    public double getBal() {
        return bal;
    }
    public void setBal(double bal) {
        this.bal = bal;
    }

    public double deposit(double amount){
        return bal+amount;
    }

    public void withdraw(double amount){
        if(amount>=bal){
            this.bal = this.bal-amount;
            System.out.println("ammount withdrawn, Balance is "+this.bal);
        }else{
            System.out.println("amount not enough");
        }
    }

    public void checkBal() {
        System.out.println("Balance is "+this.bal);
    }
}

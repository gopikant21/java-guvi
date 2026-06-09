package PaymentInterface;

public class UPI implements Payment{
    @Override
    public void pay(double amount) {
        System.out.println("UPI paid: " + amount);
    }
}

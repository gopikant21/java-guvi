package PaymentInterface;

public class CreditCard implements Payment{
    @Override
    public void pay(double amount) {
        System.out.println("Credit card has been paid for " + amount);
    }
}

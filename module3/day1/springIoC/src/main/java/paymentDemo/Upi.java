package paymentDemo;

public class Upi implements PaymentService{
    @Override
    public void pay(double amount) {
        System.out.println("Upi payment done: " + amount);
    }
}

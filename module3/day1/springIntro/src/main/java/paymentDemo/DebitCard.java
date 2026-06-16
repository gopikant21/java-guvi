package paymentDemo;

public class DebitCard implements PaymentService{

    @Override
    public void pay(double amount) {
        System.out.println("DebitCard Payment Service: " + amount);
    }
}

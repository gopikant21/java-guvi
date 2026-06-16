package demo.service;

public class DebitCard implements PaymentService{

    @Override
    public void pay(double amount) {
        System.out.println("DebitCard Payment Service: " + amount);
    }
}

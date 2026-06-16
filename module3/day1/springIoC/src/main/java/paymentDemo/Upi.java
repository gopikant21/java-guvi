package paymentDemo;

import org.springframework.stereotype.Component;

@Component("Upi")
public class Upi implements PaymentService{
    @Override
    public void pay(double amount) {
        System.out.println("Upi payment done: " + amount);
    }
}

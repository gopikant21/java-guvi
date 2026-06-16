package paymentDemo;

import org.springframework.stereotype.Component;

@Component("Credit")
public class CreditCard implements PaymentService
{
    @Override
    public void pay(double amount) {
        System.out.println("Paid " + amount + " using Credit Card");
    }
}

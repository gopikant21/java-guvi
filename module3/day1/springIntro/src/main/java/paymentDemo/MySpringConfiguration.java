package paymentDemo;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MySpringConfiguration {

    @Bean("Credit")
    public PaymentService creditPaymentService(){
        return new CreditCard();
    }

    @Bean("Debit")
    public PaymentService debitPaymentService(){
        return new DebitCard();
    }

    @Bean("Upi")
    public PaymentService upiPaymentService(){
        return new Upi();
    }

    @Bean("Text")
    public NotificationService textNotificationService(){
        return new TextNotification();
    }

    @Bean("Email")
    public NotificationService emailNotificationService(){
        return new EmailNotification();
    }

    @Bean("Whatsapp")
    public NotificationService whatsappNotificationService(){
        return new WhatsappNotification();
    }
}
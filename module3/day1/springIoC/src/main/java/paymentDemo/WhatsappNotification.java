package paymentDemo;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component("Whatsapp")
public class WhatsappNotification implements NotificationService {
    @Override
    public void sendNotification(String message){
        System.out.println("Whatsapp notification sent: " + message);
    }
}

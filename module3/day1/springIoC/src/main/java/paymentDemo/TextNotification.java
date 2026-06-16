package paymentDemo;

import org.springframework.stereotype.Component;

@Component("Text")
public class TextNotification implements NotificationService{
    @Override
    public void sendNotification(String message){
        System.out.println("Text notification sent: " + message);
    }
}

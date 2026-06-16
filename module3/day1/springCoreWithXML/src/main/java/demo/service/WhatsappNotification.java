package demo.service;

public class WhatsappNotification implements NotificationService {
    @Override
    public void sendNotification(String message){
        System.out.println("Whatsapp notification sent: " + message);
    }

}

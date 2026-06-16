package paymentDemo;

public class EmailNotification implements NotificationService{

    @Override
    public void sendNotification(String message) {
        System.out.println("Email notification sent: " + message);
    }
}

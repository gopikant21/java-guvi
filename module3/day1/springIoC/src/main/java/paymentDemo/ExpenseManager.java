package paymentDemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class ExpenseManager {
    @Autowired
    @Qualifier("Debit")
    PaymentService paymentService;

    @Autowired
    @Qualifier("Text")
    NotificationService notificationService;

    /*public ExpenseManager(PaymentService paymentService, NotificationService notificationService) {
        this.paymentService = paymentService;
        this.notificationService = notificationService;
    }*/

    public void payElectricityBill(double amount) {
        notificationService.sendNotification("Paying electricity bill of " + amount);
        paymentService.pay(amount);
        notificationService.sendNotification("Electricity bill paid");
    }

    public void payWaterBill(double amount) {
        notificationService.sendNotification("Paying water bill of " + amount);
        paymentService.pay(amount);
        notificationService.sendNotification("Water bill paid");
    }

    public void payGasBill(double amount) {
        notificationService.sendNotification("Paying gas bill of " + amount);
        paymentService.pay(amount);
        notificationService.sendNotification("Gas bill paid");
    }
}

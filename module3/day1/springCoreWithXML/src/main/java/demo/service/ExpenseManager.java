package demo.service;

public class ExpenseManager {
    private PaymentService paymentService;
    private NotificationService notificationService;

    public ExpenseManager(PaymentService paymentService, NotificationService notificationService) {
        this.paymentService = paymentService;
        this.notificationService = notificationService;
    }

    public PaymentService getPaymentService() {
        return paymentService;
    }

    public void setPaymentService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public NotificationService getNotificationService() {
        return notificationService;
    }

    public void setNotificationService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

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

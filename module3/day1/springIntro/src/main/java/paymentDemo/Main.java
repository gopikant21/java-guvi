package paymentDemo;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        ApplicationContext context =
                new AnnotationConfigApplicationContext(MySpringConfiguration.class);

        Scanner scanner = new Scanner(System.in);

        // Take payment method input
        System.out.println("Enter Payment Method (Credit/Debit/Upi): ");
        String paymentChoice = scanner.nextLine();

        // Take notification method input
        System.out.println("Enter Notification Method (Email/Whatsapp/Text): ");
        String notificationChoice = scanner.nextLine();

        // Get beans dynamically
        PaymentService paymentService =
                (PaymentService) context.getBean(paymentChoice);

        NotificationService notificationService =
                (NotificationService) context.getBean(notificationChoice);

        ExpenseManager expenseManager =
                new ExpenseManager(paymentService, notificationService);

        // Perform operations
        expenseManager.payElectricityBill(200);
        expenseManager.payGasBill(3000);
        expenseManager.payWaterBill(100);
    }
}
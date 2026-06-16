package paymentDemo;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Scanner;


@Configuration
@ComponentScan(basePackages = "paymentDemo")
public class Main {
    public static void main(String[] args) {

        ApplicationContext context =
                new AnnotationConfigApplicationContext(Main.class);

        ExpenseManager expenseManager = context.getBean(ExpenseManager.class);

        // Perform operations
        expenseManager.payElectricityBill(200);
        expenseManager.payGasBill(3000);
        expenseManager.payWaterBill(100);
    }
}
package demo;

import demo.service.ExpenseManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        ExpenseManager expenseManager = context.getBean(ExpenseManager.class);
        expenseManager.payElectricityBill(1400);
        expenseManager.payGasBill(3000);
        expenseManager.payWaterBill(400);
    }
}

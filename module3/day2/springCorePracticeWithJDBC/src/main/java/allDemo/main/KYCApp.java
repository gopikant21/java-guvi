package allDemo.main;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import allDemo.config.SpringConfiguration;
import allDemo.controller.KYCConsoleController;

public class KYCApp {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfiguration.class);

        KYCConsoleController controller = (KYCConsoleController) context.getBean("KYCConsoleController");

        controller.start();
    }
}

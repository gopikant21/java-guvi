package allDemo.main;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import allDemo.config.SpringConfiguration;
import allDemo.controller.ProductConsoleController;

public class ProductApp {

    public static void main(String[] args) {

        ApplicationContext context =
                new AnnotationConfigApplicationContext(
                        SpringConfiguration.class);

        ProductConsoleController controller =
                context.getBean(ProductConsoleController.class);

        controller.start();
    }
}
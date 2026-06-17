package allDemo.main;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import allDemo.config.SpringConfiguration;
import allDemo.controller.BookConsoleController;

public class BookApp {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfiguration.class);

        BookConsoleController controller = context.getBean(BookConsoleController.class);

        controller.start();
    }
}

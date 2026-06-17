package allDemo.main;

import allDemo.config.SpringConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import allDemo.controller.FlightConsoleController;

public class FlightApp {

    public static void main(String[] args) {

        ApplicationContext context =
                new AnnotationConfigApplicationContext(SpringConfiguration.class);

        FlightConsoleController controller =
                context.getBean(FlightConsoleController.class);

        controller.start();
    }
}

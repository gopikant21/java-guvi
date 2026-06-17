package CreditScoreDemo.main;

import CreditScoreDemo.config.JavaConfiguration;
import CreditScoreDemo.service.CreditScoreService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class CreditScoreApp {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(JavaConfiguration.class);

        CreditScoreService service =
                (CreditScoreService) context.getBean("creditScoreService");

        service.createScore(101, 780);
        service.createScore(102, 620);
        service.createScore(103, 500);

        System.out.println(service.getAllScores());

    }
}

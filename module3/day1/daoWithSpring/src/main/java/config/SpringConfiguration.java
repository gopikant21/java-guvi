package config;

import dao.CreditScoreDAO;
import dao.CreditScoreDAOImpl;
import dao.TodoDao;
import dao.TodoDaoImplCollections;
import entity.CreditScore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import services.CreditScoreService;

@Configuration
public class SpringConfiguration {

    @Bean
    public TodoDao todoDao() {
        return new TodoDaoImplCollections();
    }

    @Bean
    public CreditScoreDAO creditScoreDAO() {
        return new CreditScoreDAOImpl();
    }

    @Bean
    public CreditScoreService creditScoreService() {
        return new CreditScoreService(creditScoreDAO());
    }
}

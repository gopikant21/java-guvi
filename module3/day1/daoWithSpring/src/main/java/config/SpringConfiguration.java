package config;

import dao.TodoDao;
import dao.TodoDaoImplCollections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfiguration {
    @Bean
    public TodoDao todoDao() {
        return new TodoDaoImplCollections();
    }
}

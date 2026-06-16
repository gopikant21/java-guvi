package config;

import dao.TodoDao;
import dao.TodoDaoImplJdbc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfiguration {
    @Bean
    public TodoDao todoDao() {
        return new TodoDaoImplJdbc();
    }
}

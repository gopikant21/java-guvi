package config;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

public class DbManager {

    private static DataSource dataSource;
    private static JdbcTemplate jdbcTemplate;

    static {
        // Initialize DataSource
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("org.postgresql.Driver");
        ds.setUrl("jdbc:postgresql://localhost:5432/todo_db");
        ds.setUsername("postgres");
        ds.setPassword("12345");

        dataSource = ds;

        // Initialize JdbcTemplate
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public static DataSource getDataSource() {
        return dataSource;
    }

    public static JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }
}

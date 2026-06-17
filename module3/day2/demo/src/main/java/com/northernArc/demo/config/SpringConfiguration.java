package com.northernArc.demo.config;

import com.northernArc.demo.dao.TodoDAO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Scanner;

@Configuration
public class SpringConfiguration {
    @Bean
    public Scanner scanner() {
        return new Scanner(System.in);
    }
}

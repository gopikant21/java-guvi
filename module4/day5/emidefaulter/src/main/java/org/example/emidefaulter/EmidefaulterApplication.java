package org.example.emidefaulter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EmidefaulterApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmidefaulterApplication.class, args);
    }

}

package org.example.unittestingdemo;

import org.example.unittestingdemo.model.Product;
import org.example.unittestingdemo.service.ProductService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;

@SpringBootApplication
public class UnitTestingDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(UnitTestingDemoApplication.class, args);
    }

    @Bean
    CommandLineRunner seedProducts(ProductService productService) {
        return args -> {
            productService.createProduct(new Product(1L, "Pen", new BigDecimal("10.00")));
            productService.createProduct(new Product(2L, "Book", new BigDecimal("120.00")));
        };
    }

}

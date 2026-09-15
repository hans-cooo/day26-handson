package com.example.ecommerceapp.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.ecommerceapp.model.Product;
import com.example.ecommerceapp.repository.ProductRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner loadData(ProductRepository productRepository) {
        return args -> {
            productRepository.save(new Product("Laptop", 75000.00, 10));
            productRepository.save(new Product("Wireless Mouse", 1500.00, 25));
            productRepository.save(new Product("Keyboard", 2500.00, 20));
            productRepository.save(new Product("Monitor", 12000.00, 15));
            productRepository.save(new Product("USB-C Cable", 800.00, 50));
        };
    }
}
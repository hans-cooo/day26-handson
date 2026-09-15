package com.example.ecommerceapp.config;

import java.util.List;
import java.util.Random;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.ecommerceapp.model.Order;
import com.example.ecommerceapp.model.Product;
import com.example.ecommerceapp.repository.OrderRepository;
import com.example.ecommerceapp.repository.ProductRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner loadData(ProductRepository productRepository, OrderRepository orderRepository) {
        return args -> {
            // Seed products if database is empty
            if (productRepository.count() == 0) {
                productRepository.save(new Product("Laptop", 75000.00, 10));
                productRepository.save(new Product("Wireless Mouse", 1500.00, 25));
                productRepository.save(new Product("Keyboard", 2500.00, 20));
                productRepository.save(new Product("Monitor", 12000.00, 15));
                productRepository.save(new Product("USB-C Cable", 800.00, 50));
            }

            // Seed initial random orders if table is empty
            if (orderRepository.count() == 0) {
                List<Product> products = productRepository.findAll();
                if (!products.isEmpty()) {
                    List<String> customers = List.of("Alice", "Bob", "Charlie", "Diana");
                    Random random = new Random();

                    for (int i = 0; i < 5; i++) {
                        Product randomProduct = products.get(random.nextInt(products.size()));
                        int quantity = random.nextInt(3) + 1;
                        double totalPrice = randomProduct.getPrice() * quantity;
                        String customer = customers.get(random.nextInt(customers.size()));

                        Order order = new Order(randomProduct.getId(), quantity, customer, totalPrice);
                        orderRepository.save(order);
                    }
                }
            }
        };
    }
}
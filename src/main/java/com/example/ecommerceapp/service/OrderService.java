package com.example.ecommerceapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.ecommerceapp.model.Order;
import com.example.ecommerceapp.model.Product;
import com.example.ecommerceapp.repository.OrderRepository;
import com.example.ecommerceapp.repository.ProductRepository;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository,
                        ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public Order createOrder(Order order) {
        Product product = productRepository.findById(order.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        double totalPrice = product.getPrice() * order.getQuantity();
        order.setTotalPrice(totalPrice);

        return orderRepository.save(order);
    }
}
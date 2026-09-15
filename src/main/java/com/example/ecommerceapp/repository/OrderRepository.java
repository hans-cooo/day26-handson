package com.example.ecommerceapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ecommerceapp.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
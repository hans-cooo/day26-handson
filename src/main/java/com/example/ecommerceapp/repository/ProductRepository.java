package com.example.ecommerceapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ecommerceapp.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
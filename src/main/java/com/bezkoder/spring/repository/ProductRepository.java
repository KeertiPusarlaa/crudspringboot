package com.bezkoder.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bezkoder.spring.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}

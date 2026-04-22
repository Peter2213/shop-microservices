package com.example.products_rest_api.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.products_rest_api.Entities.Product;
@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>{
    
}

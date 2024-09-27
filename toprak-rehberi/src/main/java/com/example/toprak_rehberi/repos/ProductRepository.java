package com.example.toprak_rehberi.repos;

import com.example.toprak_rehberi.dto.ProductSuggestionDto;
import com.example.toprak_rehberi.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
}

package com.example.toprak_rehberi.controllers;

import com.example.toprak_rehberi.entities.Product;
import com.example.toprak_rehberi.repos.ProductRepository;
import com.example.toprak_rehberi.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;


    // list all products
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.status(201).body(product);
    }


    @PostMapping("/add")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product createdProduct = productService.createProduct(product);
        return ResponseEntity.status(201).body(createdProduct);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        Product updatedProduct = productService.updateProduct(id, product);
        return ResponseEntity.ok(updatedProduct);
    }

    // delete product
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/suggestions/{neighbourhoodId}")
    public List<Map<String, Object>> suggestions(@PathVariable int neighbourhoodId) {
        String sql = "SELECT p.name AS urun, " +
                "ROUND(AVG(CASE " +
                "             WHEN h.harvest_quality = 'COK_IYI' THEN 80 " +
                "             WHEN h.harvest_quality = 'NE_IYI_NE_KOTU' THEN 60 " +
                "             ELSE 30 " +
                "           END), 2) AS average_success_rate " +
                "FROM lands AS l " +
                "JOIN harvest AS h ON h.land_id = l.id " +
                "JOIN product AS p ON p.id = h.product_id " +
                "JOIN neighbourhoods AS n ON n.id = l.neighbourhood_id " +
                "WHERE n.id = ? " +
                "GROUP BY p.name " +
                "ORDER BY average_success_rate DESC " +
                "LIMIT 10";


        Object[] params = {neighbourhoodId};
        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, params);
        return results;
    }






}

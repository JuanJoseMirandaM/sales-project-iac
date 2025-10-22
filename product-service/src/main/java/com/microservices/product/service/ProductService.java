package com.microservices.product.service;

import com.microservices.product.model.Product;
import com.microservices.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(String id) {
        return productRepository.findById(id);
    }

    public Product createProduct(Product product) {
        // Check if product code already exists
        if (productRepository.existsByCode(product.getCode())) {
            throw new RuntimeException("Product code already exists: " + product.getCode());
        }
        return productRepository.save(product);
    }

    public Optional<Product> getProductByCode(String code) {
        return productRepository.findByCode(code);
    }

    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }
}

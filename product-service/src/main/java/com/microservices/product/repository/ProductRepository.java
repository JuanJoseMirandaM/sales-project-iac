package com.microservices.product.repository;

import com.microservices.product.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    Optional<Product> findByCode(String code);

    List<Product> findByCategory(String category);

    boolean existsByCode(String code);
}

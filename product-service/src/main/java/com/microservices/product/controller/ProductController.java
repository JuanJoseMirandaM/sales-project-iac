package com.microservices.product.controller;

import com.microservices.product.dto.ProductRequest;
import com.microservices.product.dto.ProductResponse;
import com.microservices.product.model.Product;
import com.microservices.product.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        List<ProductResponse> productResponses = products.stream()
            .map(this::convertToProductResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(productResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable String id) {
        Optional<Product> product = productService.getProductById(id);
        return product.map(p -> ResponseEntity.ok(convertToProductResponse(p)))
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest productRequest) {
        Product product = convertToProduct(productRequest);
        Product createdProduct = productService.createProduct(product);
        ProductResponse productResponse = convertToProductResponse(createdProduct);
        return ResponseEntity.status(HttpStatus.CREATED).body(productResponse);
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<ProductResponse> getProductByCode(@PathVariable String code) {
        Optional<Product> product = productService.getProductByCode(code);
        return product.map(p -> ResponseEntity.ok(convertToProductResponse(p)))
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductResponse>> getProductsByCategory(@PathVariable String category) {
        List<Product> products = productService.getProductsByCategory(category);
        List<ProductResponse> productResponses = products.stream()
            .map(this::convertToProductResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(productResponses);
    }

    // Métodos de conversión
    private ProductResponse convertToProductResponse(Product product) {
        return new ProductResponse(
            product.getId(),
            product.getCode(),
            product.getName(),
            product.getDescription(),
            product.getCategory(),
            product.getPrice()
        );
    }

    private Product convertToProduct(ProductRequest productRequest) {
        Product product = new Product();
        product.setCode(productRequest.getCode());
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setCategory(productRequest.getCategory());
        product.setPrice(productRequest.getPrice());
        return product;
    }
}

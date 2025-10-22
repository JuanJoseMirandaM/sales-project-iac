package com.microservices.sale.controller;

import com.microservices.sale.dto.SaleRequest;
import com.microservices.sale.dto.SaleResponse;
import com.microservices.sale.dto.DetailSaleRequest;
import com.microservices.sale.dto.DetailSaleResponse;
import com.microservices.sale.model.Sale;
import com.microservices.sale.model.DetailSale;
import com.microservices.sale.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sales")
@CrossOrigin(origins = "*")
public class SaleController {
    
    @Autowired
    private SaleService saleService;
    
    @GetMapping
    public ResponseEntity<List<SaleResponse>> getAllSales() {
        List<Sale> sales = saleService.getAllSales();
        List<SaleResponse> saleResponses = sales.stream()
                .map(this::convertToSaleResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(saleResponses);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<SaleResponse> getSaleById(@PathVariable String id) {
        Optional<Sale> sale = saleService.getSaleById(id);
        return sale.map(s -> ResponseEntity.ok(convertToSaleResponse(s)))
                  .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<SaleResponse> createSale(@Valid @RequestBody SaleRequest saleRequest) {
        Sale sale = convertToSale(saleRequest);
        Sale createdSale = saleService.createSale(sale);
        SaleResponse saleResponse = convertToSaleResponse(createdSale);
        return ResponseEntity.status(HttpStatus.CREATED).body(saleResponse);
    }
    
    @GetMapping("/customer/{customer}")
    public ResponseEntity<List<SaleResponse>> getSalesByCustomer(@PathVariable String customer) {
        List<Sale> sales = saleService.getSalesByCustomer(customer);
        List<SaleResponse> saleResponses = sales.stream()
                .map(this::convertToSaleResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(saleResponses);
    }
    
    @GetMapping("/date-range")
    public ResponseEntity<List<SaleResponse>> getSalesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<Sale> sales = saleService.getSalesByDateRange(startDate, endDate);
        List<SaleResponse> saleResponses = sales.stream()
                .map(this::convertToSaleResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(saleResponses);
    }
    
    // Métodos de conversión
    private SaleResponse convertToSaleResponse(Sale sale) {
        List<DetailSaleResponse> detailSaleResponses = sale.getItems().stream()
                .map(this::convertToDetailSaleResponse)
                .collect(Collectors.toList());
        
        return new SaleResponse(
                sale.getId(),
                sale.getCustomer(),
                sale.getDate(),
                sale.getTotal(),
                detailSaleResponses
        );
    }
    
    private DetailSaleResponse convertToDetailSaleResponse(DetailSale detailSale) {
        return new DetailSaleResponse(
                detailSale.getQuantity(),
                detailSale.getProductName(),
                detailSale.getProductCode(),
                detailSale.getPrice(),
                detailSale.getSubtotal()
        );
    }
    
    private Sale convertToSale(SaleRequest saleRequest) {
        List<DetailSale> detailSales = saleRequest.getItems().stream()
                .map(this::convertToDetailSale)
                .collect(Collectors.toList());
        
        Sale sale = new Sale();
        sale.setCustomer(saleRequest.getCustomer());
        sale.setDate(saleRequest.getDate());
        sale.setTotal(saleRequest.getTotal());
        sale.setItems(detailSales);
        return sale;
    }
    
    private DetailSale convertToDetailSale(DetailSaleRequest detailSaleRequest) {
        DetailSale detailSale = new DetailSale();
        detailSale.setQuantity(detailSaleRequest.getQuantity());
        detailSale.setProductName(detailSaleRequest.getProductName());
        detailSale.setProductCode(detailSaleRequest.getProductCode());
        detailSale.setPrice(detailSaleRequest.getPrice());
        detailSale.setSubtotal(detailSaleRequest.getSubtotal());
        return detailSale;
    }
}

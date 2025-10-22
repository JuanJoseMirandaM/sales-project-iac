package com.microservices.sale.service;

import com.microservices.sale.model.Sale;
import com.microservices.sale.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SaleService {
    
    @Autowired
    private SaleRepository saleRepository;
    
    public List<Sale> getAllSales() {
        return saleRepository.findAll();
    }
    
    public Optional<Sale> getSaleById(String id) {
        return saleRepository.findById(id);
    }
    
    public Sale createSale(Sale sale) {
        // Set current date if not provided
        if (sale.getDate() == null) {
            sale.setDate(LocalDateTime.now());
        }
        return saleRepository.save(sale);
    }
    
    public List<Sale> getSalesByCustomer(String customer) {
        return saleRepository.findByCustomer(customer);
    }
    
    public List<Sale> getSalesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return saleRepository.findByDateBetween(startDate, endDate);
    }
}

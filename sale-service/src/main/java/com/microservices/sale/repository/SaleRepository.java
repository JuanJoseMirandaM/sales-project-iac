package com.microservices.sale.repository;

import com.microservices.sale.model.Sale;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SaleRepository extends MongoRepository<Sale, String> {
    List<Sale> findByCustomer(String customer);
    List<Sale> findByDateBetween(java.time.LocalDateTime startDate, java.time.LocalDateTime endDate);
}

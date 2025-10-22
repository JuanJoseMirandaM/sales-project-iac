package com.microservices.sale.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleResponse {
    
    private String id;
    private String customer;
    private LocalDateTime date;
    private BigDecimal total;
    private List<DetailSaleResponse> items;
}

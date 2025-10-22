package com.microservices.sale.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailSaleResponse {
    
    private Integer quantity;
    private String productName;
    private String productCode;
    private BigDecimal price;
    private BigDecimal subtotal;
}

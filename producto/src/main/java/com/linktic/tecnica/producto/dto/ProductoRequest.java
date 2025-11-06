package com.linktic.tecnica.producto.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoRequest {
    
    private String name;
    
    private BigDecimal precio;
    
    private String sku;
}
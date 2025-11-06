package com.linktic.tecnica.producto.config;

import com.linktic.tecnica.producto.model.Producto;
import com.linktic.tecnica.producto.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final ProductoRepository productoRepository;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            // Crear el producto de ejemplo si no existe
            if (productoRepository.count() == 0) {
                Producto laptopDell = Producto.builder()
                        .name("Laptop Dell XPS 15")
                        .precio(new BigDecimal("1299.99"))
                        .sku("LAP-DELL-XPS15-001")
                        .build();
                
                productoRepository.save(laptopDell);
            }
        };
    }
}
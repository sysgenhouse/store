package com.linktic.tecnica.inventario.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.linktic.tecnica.inventario.model.Inventario;
import com.linktic.tecnica.inventario.repository.InventarioRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final InventarioRepository inventarioRepository;

    @Override
    public void run(String... args) throws Exception {
        // Crear datos de ejemplo si no existen
        if (inventarioRepository.count() == 0) {
            // Crear inventario para el producto 1 (Laptop Dell XPS 15)
            Inventario inventarioLaptop = Inventario.builder()
                    .productoId(1L)
                    .cantidad(50)
                    .ubicacion("Almacén A")
                    .build();

            inventarioRepository.save(inventarioLaptop);

            // Crear inventario para el producto 2 (Smartphone Samsung Galaxy S23)
            Inventario inventarioPhone = Inventario.builder()
                    .productoId(2L)
                    .cantidad(100)
                    .ubicacion("Almacén B")
                    .build();

            inventarioRepository.save(inventarioPhone);
        }
    }
}
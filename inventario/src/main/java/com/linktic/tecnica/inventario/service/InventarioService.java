package com.linktic.tecnica.inventario.service;

import com.linktic.tecnica.inventario.dto.ActualizarCantidadRequest;
import com.linktic.tecnica.inventario.dto.InventarioRequest;
import com.linktic.tecnica.inventario.event.InventarioEvent;
import com.linktic.tecnica.inventario.model.Inventario;
import com.linktic.tecnica.inventario.repository.InventarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventarioService {

    private final InventarioRepository inventarioRepository;
    private final RestTemplate restTemplate;
    private final ApplicationEventPublisher eventPublisher;

    public Optional<Inventario> getInventarioByProductoId(Long productoId) {
        return inventarioRepository.findByProductoId(productoId);
    }

    public Inventario createInventario(InventarioRequest inventarioRequest) {
        Inventario inventario = Inventario.builder()
                .productoId(inventarioRequest.getProductoId())
                .cantidad(inventarioRequest.getCantidad())
                .ubicacion(inventarioRequest.getUbicacion())
                .build();
        
        Inventario savedInventario = inventarioRepository.save(inventario);
        
        // Emitir evento de creación de inventario
        eventPublisher.publishEvent(new InventarioEvent(savedInventario, "CREADO"));
        
        return savedInventario;
    }

    public Optional<Inventario> actualizarCantidad(Long productoId, ActualizarCantidadRequest request) {
        Optional<Inventario> inventarioOptional = inventarioRepository.findByProductoId(productoId);
        if (inventarioOptional.isPresent()) {
            Inventario inventario = inventarioOptional.get();
            Integer cantidadAnterior = inventario.getCantidad();
            inventario.setCantidad(request.getCantidad());
            
            Inventario updatedInventario = inventarioRepository.save(inventario);
            
            // Emitir evento de actualización de inventario
            eventPublisher.publishEvent(new InventarioEvent(updatedInventario, "ACTUALIZADO"));
            
            log.info("Cantidad actualizada para producto {}: {} -> {}", 
                    productoId, cantidadAnterior, request.getCantidad());
            
            return Optional.of(updatedInventario);
        }
        return Optional.empty();
    }

    public boolean reducirStock(Long productoId, Integer cantidad) {
        Optional<Inventario> inventarioOptional = inventarioRepository.findByProductoId(productoId);
        if (inventarioOptional.isPresent()) {
            Inventario inventario = inventarioOptional.get();
            if (inventario.getCantidad() >= cantidad) {
                Integer cantidadAnterior = inventario.getCantidad();
                inventario.setCantidad(inventario.getCantidad() - cantidad);
                
                Inventario updatedInventario = inventarioRepository.save(inventario);
                
                // Emitir evento de reducción de stock
                eventPublisher.publishEvent(new InventarioEvent(updatedInventario, "REDUCIDO"));
                
                log.info("Stock reducido para producto {}: {} -> {}", 
                        productoId, cantidadAnterior, inventario.getCantidad());
                
                return true;
            } else {
                log.warn("Stock insuficiente para producto {}. Disponible: {}, Solicitado: {}", 
                        productoId, inventario.getCantidad(), cantidad);
            }
        }
        return false;
    }

    public Map<String, Object> getProductoInfo(Long productoId) {
        try {
            String url = "http://localhost:8080/api/productos/" + productoId;
            return restTemplate.getForObject(url, Map.class);
        } catch (Exception e) {
            log.error("Error al obtener información del producto {}: {}", productoId, e.getMessage());
            return Map.of(); // Devolver un mapa vacío en caso de error
        }
    }
}
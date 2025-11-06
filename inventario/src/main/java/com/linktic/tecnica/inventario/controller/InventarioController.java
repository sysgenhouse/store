package com.linktic.tecnica.inventario.controller;

import com.linktic.tecnica.inventario.dto.ActualizarCantidadRequest;
import com.linktic.tecnica.inventario.dto.InventarioRequest;
import com.linktic.tecnica.inventario.model.Inventario;
import com.linktic.tecnica.inventario.service.InventarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/inventarios")
@RequiredArgsConstructor
@Slf4j
public class InventarioController {

    private final InventarioService inventarioService;

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<?> getCantidadByProductoId(@PathVariable Long productoId) {
        Optional<Inventario> inventario = inventarioService.getInventarioByProductoId(productoId);
        if (inventario.isPresent()) {
            // Obtener información adicional del producto
            Map<String, Object> productoInfo = inventarioService.getProductoInfo(productoId);
            
            Map<String, Object> response = new java.util.HashMap<>();
            response.put("inventario", inventario.get());
            if (productoInfo != null) {
                response.put("producto", productoInfo);
            }
            
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Inventario> createInventario(@RequestBody InventarioRequest inventarioRequest) {
        Inventario nuevoInventario = inventarioService.createInventario(inventarioRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoInventario);
    }

    @PutMapping("/producto/{productoId}/cantidad")
    public ResponseEntity<?> actualizarCantidad(
            @PathVariable Long productoId,
            @RequestBody ActualizarCantidadRequest request) {
        
        Optional<Inventario> inventarioActualizado = inventarioService.actualizarCantidad(productoId, request);
        return inventarioActualizado.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/producto/{productoId}/compra")
    public ResponseEntity<?> procesarCompra(
            @PathVariable Long productoId,
            @RequestParam Integer cantidad) {
        
        boolean exito = inventarioService.reducirStock(productoId, cantidad);
        if (exito) {
            return ResponseEntity.ok(Map.of(
                    "message", "Compra procesada exitosamente",
                    "productoId", productoId,
                    "cantidadComprada", cantidad
            ));
        } else {
            return ResponseEntity.badRequest().body(Map.of(
                    "message", "Stock insuficiente",
                    "productoId", productoId,
                    "cantidadSolicitada", cantidad
            ));
        }
    }

    @GetMapping("/producto/{productoId}/disponible")
    public ResponseEntity<?> verificarDisponibilidad(
            @PathVariable Long productoId,
            @RequestParam Integer cantidad) {
        
        Optional<Inventario> inventario = inventarioService.getInventarioByProductoId(productoId);
        if (inventario.isPresent()) {
            boolean disponible = inventario.get().getCantidad() >= cantidad;
            return ResponseEntity.ok(Map.of(
                    "productoId", productoId,
                    "cantidadSolicitada", cantidad,
                    "cantidadDisponible", inventario.get().getCantidad(),
                    "disponible", disponible
            ));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
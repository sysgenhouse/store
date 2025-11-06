package com.linktic.tecnica.producto.controller;

import com.linktic.tecnica.producto.dto.ProductoRequest;
import com.linktic.tecnica.producto.dto.ProductoUpdateRequest;
import com.linktic.tecnica.producto.model.Producto;
import com.linktic.tecnica.producto.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    @GetMapping
    public ResponseEntity<List<Producto>> getAllProductos() {
        List<Producto> productos = productoService.getAllProductos();
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> getProductoById(@PathVariable Long id) {
        Optional<Producto> producto = productoService.getProductoById(id);
        return producto.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Producto> createProducto(@RequestBody ProductoRequest productoRequest) {
        Producto nuevoProducto = productoService.createProducto(productoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProducto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> updateProducto(@PathVariable Long id, @RequestBody ProductoRequest productoRequest) {
        Producto productoActualizado = productoService.updateProducto(id, productoRequest);
        if (productoActualizado != null) {
            return ResponseEntity.ok(productoActualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProducto(@PathVariable Long id) {
        boolean eliminado = productoService.deleteProducto(id);
        if (eliminado) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Producto> updateProductoParcial(@PathVariable Long id, @RequestBody ProductoUpdateRequest productoUpdateRequest) {
        Optional<Producto> productoActualizado = productoService.updateProductoParcial(id, productoUpdateRequest);
        return productoActualizado.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
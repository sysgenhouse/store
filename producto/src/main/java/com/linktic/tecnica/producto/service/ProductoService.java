package com.linktic.tecnica.producto.service;

import com.linktic.tecnica.producto.dto.ProductoRequest;
import com.linktic.tecnica.producto.dto.ProductoUpdateRequest;
import com.linktic.tecnica.producto.model.Producto;
import com.linktic.tecnica.producto.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;

    public List<Producto> getAllProductos() {
        return productoRepository.findAll();
    }

    public Optional<Producto> getProductoById(Long id) {
        return productoRepository.findById(id);
    }

    public Producto createProducto(ProductoRequest productoRequest) {
        Producto producto = Producto.builder()
                .name(productoRequest.getName())
                .precio(productoRequest.getPrecio())
                .sku(productoRequest.getSku())
                .build();
        return productoRepository.save(producto);
    }

    public Producto updateProducto(Long id, ProductoRequest productoRequest) {
        Optional<Producto> productoOptional = productoRepository.findById(id);
        if (productoOptional.isPresent()) {
            Producto producto = productoOptional.get();
            producto.setName(productoRequest.getName());
            producto.setPrecio(productoRequest.getPrecio());
            producto.setSku(productoRequest.getSku());
            return productoRepository.save(producto);
        }
        return null;
    }

    public boolean deleteProducto(Long id) {
        if (productoRepository.existsById(id)) {
            productoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional<Producto> updateProductoParcial(Long id, ProductoUpdateRequest productoUpdateRequest) {
        Optional<Producto> productoOptional = productoRepository.findById(id);
        if (productoOptional.isPresent()) {
            Producto producto = productoOptional.get();
            
            // Actualizar solo los campos que no son nulos
            if (productoUpdateRequest.getName() != null) {
                producto.setName(productoUpdateRequest.getName());
            }
            if (productoUpdateRequest.getPrecio() != null) {
                producto.setPrecio(productoUpdateRequest.getPrecio());
            }
            if (productoUpdateRequest.getSku() != null) {
                producto.setSku(productoUpdateRequest.getSku());
            }
            
            return Optional.of(productoRepository.save(producto));
        }
        return Optional.empty();
    }
}
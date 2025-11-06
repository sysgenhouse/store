package com.linktic.tecnica.inventario.event;

import com.linktic.tecnica.inventario.model.Inventario;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class InventarioEvent {
    
    private Inventario inventario;
    
    private String tipoOperacion;
    
    private LocalDateTime timestamp;
    
    public InventarioEvent(Inventario inventario, String tipoOperacion) {
        this.inventario = inventario;
        this.tipoOperacion = tipoOperacion;
        this.timestamp = LocalDateTime.now();
    }
}
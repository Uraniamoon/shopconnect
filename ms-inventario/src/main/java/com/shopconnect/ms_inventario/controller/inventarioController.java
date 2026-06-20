package com.shopconnect.ms_inventario.controller;

import com.shopconnect.ms_inventario.Dto.InventarioDTO;
import com.shopconnect.ms_inventario.service.inventarioService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/inventario")
public class inventarioController {

    // Dependencia de la capa de negocio (Service)
    private final inventarioService service;

    // Inyección por constructor (Buena práctica obligatoria en la rúbrica)
    public inventarioController(inventarioService service) {
        this.service = service;
    }

    // GET: Para consultar las existencias reales desde la base de datos Oracle
    @GetMapping
    public List<InventarioDTO> obtenerInventario() {
        return service.obtenerInventarioReal();
    }

    // POST: Para guardar o actualizar registros reales en Oracle aplicando reglas de negocio
    @PostMapping
    public InventarioDTO agregarStock(@RequestBody InventarioDTO nuevoInventarioDTO) {
        return service.guardarStockReal(nuevoInventarioDTO);
    }
}
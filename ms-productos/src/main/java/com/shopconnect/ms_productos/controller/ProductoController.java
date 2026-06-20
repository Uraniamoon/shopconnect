package com.shopconnect.ms_productos.controller;

import com.shopconnect.ms_productos.Dto.ProductoDTO;
import com.shopconnect.ms_productos.service.productosService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final productosService service;

    // Inyección por constructor de la capa de lógica (Service)
    public ProductoController(productosService service) {
        this.service = service;
    }

    // GET: Entrega el catálogo real consultando a la capa de negocio
    @GetMapping
    public List<ProductoDTO> obtenerCatalogo() {
        return service.obtenerProductosReales();
    }

    // POST: Recibe un nuevo producto en JSON desde Postman y lo guarda en Oracle XE
    @PostMapping
    public ProductoDTO agregarProducto(@RequestBody ProductoDTO nuevoProductoDTO) {
        return service.guardarProductoReal(nuevoProductoDTO);
    }
}
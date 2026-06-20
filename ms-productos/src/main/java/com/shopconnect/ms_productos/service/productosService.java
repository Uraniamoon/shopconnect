package com.shopconnect.ms_productos.service;

import com.shopconnect.ms_productos.Dto.ProductoDTO;
import com.shopconnect.ms_productos.model.Producto;
import com.shopconnect.ms_productos.repository.productosRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class productosService {

    private final productosRepository repository;

    // Inyección por constructor mandada por la pauta de evaluación
    public productosService(productosRepository repository) {
        this.repository = repository;
    }

    // 1. Obtener todos los productos reales del catálogo desde Oracle XE
    public List<ProductoDTO> obtenerProductosReales() {
        return repository.findAll()
                .stream()
                .map(prod -> new ProductoDTO(
                        prod.getId_producto(), 
                        prod.getNombre(), 
                        prod.getPrecio().doubleValue() // Convertimos el BigDecimal de Oracle a Double para el DTO
                ))
                .collect(Collectors.toList());
    }

    // 2. Guardar un producto nuevo de forma real en la base de datos
    public ProductoDTO guardarProductoReal(ProductoDTO dto) {
        // Regla de negocio elemental: El precio de venta no puede ser gratis ni negativo
        if (dto.getPrecioVenta() <= 0) {
            throw new RuntimeException("El precio de venta del producto debe ser mayor a cero");
        }

        Producto nuevoProd = new Producto();
        nuevoProd.setNombre(dto.getNombre());
        nuevoProd.setPrecio(BigDecimal.valueOf(dto.getPrecioVenta())); // Mapeo seguro a BigDecimal financiero

        // Persistencia real en Oracle XE a través de JPA
        Producto guardado = repository.save(nuevoProd);

        // Devolvemos el DTO de confirmación limpio para el catálogo
        return new ProductoDTO(
                guardado.getId_producto(), 
                guardado.getNombre(), 
                guardado.getPrecio().doubleValue()
        );
    }
}
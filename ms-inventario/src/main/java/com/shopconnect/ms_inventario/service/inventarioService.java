package com.shopconnect.ms_inventario.service;

import com.shopconnect.ms_inventario.Dto.InventarioDTO;
import com.shopconnect.ms_inventario.model.Inventario;
import com.shopconnect.ms_inventario.repository.inventarioRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class inventarioService {

    // 1. Inyectamos el repositorio usando el constructor (Práctica profesional recomendada)
    private final inventarioRepository repository;

    public inventarioService(inventarioRepository repository) {
        this.repository = repository;
    }

    // 2. Método para obtener todo el inventario real de Oracle XE
    public List<InventarioDTO> obtenerInventarioReal() {
        return repository.findAll() // Va a la base de datos a buscar las entidades pesadas
                .stream()
                .map(entity -> new InventarioDTO(entity.getIdInventario(), entity.getStockActual())) // Las transforma en DTOs livianos
                .collect(Collectors.toList());
    }

    // 3. Método para actualizar o agregar stock aplicando reglas de negocio
    public InventarioDTO guardarStockReal(InventarioDTO dto) {
        // Aquí puedes meter reglas de negocio: ej. verificar que el stock no sea negativo
        if (dto.getStockActual() < 0) {
            throw new RuntimeException("El stock no puede ser menor a cero");
        }

        Inventario nuevaEntidad = new Inventario();
        nuevaEntidad.setIdInventario(dto.getIdInventario());
        nuevaEntidad.setStockActual(dto.getStockActual());
        nuevaEntidad.setStockMinimo(5); // Le pones un stock mínimo por defecto en la base de datos

        Inventario guardado = repository.save(nuevaEntidad); // Guarda en Oracle XE
        return new InventarioDTO(guardado.getIdInventario(), guardado.getStockActual());
    }
}
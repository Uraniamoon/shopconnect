package com.shopconnect.ms_pedidos.service;

import com.shopconnect.ms_pedidos.Dto.PedidoDTO;
import com.shopconnect.ms_pedidos.model.Pedido;
import com.shopconnect.ms_pedidos.repository.pedidosRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class pedidosService {

    private final pedidosRepository repository;

    // Inyección por constructor mandatorio por la pauta
    public pedidosService(pedidosRepository repository) {
        this.repository = repository;
    }

    // 1. Obtener todos los pedidos reales desde Oracle XE
    public List<PedidoDTO> obtenerPedidosReales() {
        return repository.findAll()
                .stream()
                .map(pedido -> new PedidoDTO(
                        pedido.getId(), 
                        pedido.getTotal().doubleValue(), 
                        "PAGADO" // Aquí mapeas el estado según tu lógica de negocio
                ))
                .collect(Collectors.toList());
    }

    // 2. Crear y guardar un pedido real en la base de datos
    public PedidoDTO guardarPedidoReal(PedidoDTO dto) {
        // Regla de negocio: No se procesan órdenes vacías o en cero
        if (dto.getTotal() <= 0) {
            throw new RuntimeException("El total del pedido debe ser mayor a cero");
        }

        Pedido nuevoPedido = new Pedido();
        nuevoPedido.setFechaPedido(LocalDateTime.now()); // Asigna la fecha real del servidor automáticamente
        nuevoPedido.setTotal(BigDecimal.valueOf(dto.getTotal())); // Mapeo seguro a BigDecimal
        nuevoPedido.setUsuarioId(1L); // Simulación lógica del ID de usuario comprador por defecto

        // Persistencia real en la tabla mediante JPA
        Pedido pedidoGuardado = repository.save(nuevoPedido);

        // Retornamos el DTO de respuesta limpio para el cliente
        return new PedidoDTO(
                pedidoGuardado.getId(), 
                pedidoGuardado.getTotal().doubleValue(), 
                "PENDIENTE_PAGO"
        );
    }
}

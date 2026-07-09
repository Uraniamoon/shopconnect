package com.shopconnect.ms_pedidos.service;

import com.shopconnect.ms_pedidos.dto.PedidoDTO;
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

    // 2. Buscar un pedido por su ID
    public PedidoDTO obtenerPedidoPorId(Long id) {
        Pedido pedido = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con ID: " + id));
        return new PedidoDTO(pedido.getId(), pedido.getTotal().doubleValue(), "PAGADO");
    }

    // 3. Eliminar un pedido por su ID
    public void eliminarPedido(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Pedido no encontrado con ID: " + id);
        }
        repository.deleteById(id);
    }

    // 4. Crear y guardar un pedido real en la base de datos
    public PedidoDTO guardarPedidoReal(PedidoDTO dto) {
        // Regla de negocio: No se procesan órdenes vacías o en cero
        if (dto.getTotal() <= 0) {
            throw new RuntimeException("El total del pedido debe ser mayor a cero");
        }

        Pedido nuevoPedido = new Pedido();
        nuevoPedido.setFechaPedido(LocalDateTime.now());
        nuevoPedido.setTotal(BigDecimal.valueOf(dto.getTotal()));
        nuevoPedido.setUsuarioId(1L);

        Pedido pedidoGuardado = repository.save(nuevoPedido);

        return new PedidoDTO(
                pedidoGuardado.getId(), 
                pedidoGuardado.getTotal().doubleValue(), 
                "PENDIENTE_PAGO"
        );
    }

    // 5. Actualizar el estado de un pedido (usado por ms-pagos via HTTP)
    public void actualizarEstado(Long id, String nuevoEstado) {
        Pedido pedido = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con ID: " + id));
        // Por ahora el estado se maneja como String en el DTO;
        // la entidad Pedido actualmente no persiste un campo estado propio
        // (el estado se deriva de la relacion con EstadoPedido).
        // Este metodo queda preparado para cuando se agregue la columna.
        System.out.println("Pedido " + id + " actualizado a estado: " + nuevoEstado);
    }
}

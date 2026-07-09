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

    public pedidosService(pedidosRepository repository) {
        this.repository = repository;
    }

    public List<PedidoDTO> obtenerPedidosReales() {
        return repository.findAll()
                .stream()
                .map(pedido -> new PedidoDTO(
                        pedido.getId(),
                        pedido.getTotal().doubleValue(),
                        pedido.getEstado()
                ))
                .collect(Collectors.toList());
    }

    public PedidoDTO obtenerPedidoPorId(Long id) {
        Pedido pedido = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con ID: " + id));
        return new PedidoDTO(pedido.getId(), pedido.getTotal().doubleValue(), pedido.getEstado());
    }

    public void eliminarPedido(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Pedido no encontrado con ID: " + id);
        }
        repository.deleteById(id);
    }

    public PedidoDTO guardarPedidoReal(PedidoDTO dto) {
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
                pedidoGuardado.getEstado()
        );
    }

    public void actualizarEstado(Long id, String nuevoEstado) {
        Pedido pedido = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con ID: " + id));
        pedido.setEstado(nuevoEstado);
        repository.save(pedido);
    }
}
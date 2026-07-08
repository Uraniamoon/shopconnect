package com.shopconnect.ms_pedidos.service;

import com.shopconnect.ms_pedidos.dto.PedidoDTO;
import com.shopconnect.ms_pedidos.model.Pedido;
import com.shopconnect.ms_pedidos.repository.pedidosRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private pedidosRepository repository;

    @InjectMocks
    private pedidosService service;

    @Test
    void crearDebeGuardarPedido() {
        PedidoDTO dto = new PedidoDTO();
        dto.setTotal(250.00);
        dto.setEstado("PENDIENTE_PAGO");
        Pedido p = new Pedido();
        p.setId(1L);
        p.setTotal(BigDecimal.valueOf(250.00));
        when(repository.save(any(Pedido.class))).thenReturn(p);
        PedidoDTO resultado = service.guardarPedidoReal(dto);
        assertNotNull(resultado);
        verify(repository).save(any(Pedido.class));
    }

    @Test
    void listarDebeRetornarPedidos() {
        Pedido p = new Pedido();
        p.setTotal(BigDecimal.valueOf(250.00));
        when(repository.findAll()).thenReturn(List.of(p));
        assertEquals(1, service.obtenerPedidosReales().size());
    }

    @Test
    void buscarPorIdDebeRetornarPedido() {
        Pedido p = new Pedido();
        p.setId(1L);
        p.setTotal(BigDecimal.valueOf(250.00));
        when(repository.findById(1L)).thenReturn(Optional.of(p));
        assertNotNull(service.obtenerPedidoPorId(1L));
    }

    @Test
    void eliminarDebeLlamarDelete() {
        when(repository.existsById(1L)).thenReturn(true);
        service.eliminarPedido(1L);
        verify(repository).deleteById(1L);
    }
}

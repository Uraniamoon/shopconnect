package com.shopconnect.ms_pagos.service;

import com.shopconnect.ms_pagos.dto.PagoDTO;
import com.shopconnect.ms_pagos.model.Pago;
import com.shopconnect.ms_pagos.repository.pagosRepository;
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
class PagoServiceTest {

    @Mock
    private pagosRepository repository;

    @InjectMocks
    private pagosService service;

    @Test
    void crearDebeGuardarPago() {
        PagoDTO dto = new PagoDTO();
        dto.setIdPedido(1L);
        dto.setMonto(150.00);
        Pago p = new Pago();
        p.setId(1L);
        p.setPedidoId(1L);
        p.setMonto(BigDecimal.valueOf(150.00));
        when(repository.save(any(Pago.class))).thenReturn(p);
        PagoDTO resultado = service.guardarPagoReal(dto);
        assertNotNull(resultado);
        verify(repository).save(any(Pago.class));
    }

    @Test
    void listarDebeRetornarPagos() {
        Pago p = new Pago();
        p.setMonto(BigDecimal.valueOf(150.00));
        when(repository.findAll()).thenReturn(List.of(p));
        assertEquals(1, service.obtenerPagosReales().size());
    }

    @Test
    void buscarPorIdDebeRetornarPago() {
        Pago p = new Pago();
        p.setId(1L);
        p.setMonto(BigDecimal.valueOf(150.00));
        when(repository.findById(1L)).thenReturn(Optional.of(p));
        assertNotNull(service.obtenerPagoPorId(1L));
    }

    @Test
    void eliminarDebeLlamarDelete() {
        when(repository.existsById(1L)).thenReturn(true);
        service.eliminarPago(1L);
        verify(repository).deleteById(1L);
    }
}

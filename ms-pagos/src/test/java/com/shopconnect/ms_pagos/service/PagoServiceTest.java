package com.shopconnect.ms_pagos.service;

import com.shopconnect.ms_pagos.dto.EstadoUpdateDTO;
import com.shopconnect.ms_pagos.dto.PagoDTO;
import com.shopconnect.ms_pagos.dto.PedidoRemotoDTO;
import com.shopconnect.ms_pagos.model.Pago;
import com.shopconnect.ms_pagos.repository.pagosRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PagoServiceTest {

    @Mock
    private pagosRepository repository;

    @Mock
    private RestTemplate restTemplate;

    private pagosService service;

    @BeforeEach
    void setUp() {
        service = new pagosService(repository, restTemplate, "http://test:8085");
    }

    @Test
    void crearDebeGuardarPago() {
        PagoDTO dto = new PagoDTO();
        dto.setIdPedido(1L);
        dto.setMonto(150.00);

        when(restTemplate.getForObject(
                "http://test:8085/api/pedidos/{id}",
                PedidoRemotoDTO.class,
                1L
        )).thenReturn(new PedidoRemotoDTO(1L, 250.0, "PENDIENTE_PAGO"));

        Pago p = new Pago();
        p.setId(1L);
        p.setPedidoId(1L);
        p.setMonto(BigDecimal.valueOf(150.00));
        when(repository.save(any(Pago.class))).thenReturn(p);

        doNothing().when(restTemplate).put(
                eq("http://test:8085/api/pedidos/{id}/estado"),
                any(EstadoUpdateDTO.class),
                eq(1L)
        );

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

package com.shopconnect.ms_pagos.service;

import com.shopconnect.ms_pagos.dto.EstadoUpdateDTO;
import com.shopconnect.ms_pagos.dto.PagoDTO;
import com.shopconnect.ms_pagos.dto.PedidoRemotoDTO;
import com.shopconnect.ms_pagos.model.Pago;
import com.shopconnect.ms_pagos.repository.pagosRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class pagosService {

    private final pagosRepository repository;
    private final RestTemplate restTemplate;
    private final String pedidosUrl;

    public pagosService(pagosRepository repository, RestTemplate restTemplate) {
        this.repository = repository;
        this.restTemplate = restTemplate;
        this.pedidosUrl = "http://host.docker.internal:8085";
    }

    // Constructor usado por tests para inyectar URL personalizada
    public pagosService(pagosRepository repository, RestTemplate restTemplate, String pedidosUrl) {
        this.repository = repository;
        this.restTemplate = restTemplate;
        this.pedidosUrl = pedidosUrl;
    }

    public List<PagoDTO> obtenerPagosReales() {
        return repository.findAll()
                .stream()
                .map(pago -> new PagoDTO(pago.getPedidoId(), pago.getMonto().doubleValue()))
                .collect(Collectors.toList());
    }

    public PagoDTO obtenerPagoPorId(Long id) {
        Pago pago = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado con ID: " + id));
        return new PagoDTO(pago.getPedidoId(), pago.getMonto().doubleValue());
    }

    public void eliminarPago(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Pago no encontrado con ID: " + id);
        }
        repository.deleteById(id);
    }

    public PagoDTO guardarPagoReal(PagoDTO dto) {
        if (dto.getMonto() <= 0) {
            throw new RuntimeException("El monto del pago debe ser mayor a cero");
        }

        // Validar que el pedido existe via HTTP hacia ms-pedidos
        PedidoRemotoDTO pedido = restTemplate.getForObject(
                pedidosUrl + "/api/pedidos/{id}",
                PedidoRemotoDTO.class,
                dto.getIdPedido()
        );

        if (pedido == null) {
            throw new RuntimeException("Pedido con ID " + dto.getIdPedido() + " no existe");
        }

        Pago nuevoPago = new Pago();
        nuevoPago.setPedidoId(dto.getIdPedido());
        nuevoPago.setMonto(BigDecimal.valueOf(dto.getMonto()));

        Pago pagoGuardado = repository.save(nuevoPago);

        // Actualizar estado del pedido a PAGADO via HTTP
        restTemplate.put(
                pedidosUrl + "/api/pedidos/{id}/estado",
                new EstadoUpdateDTO("PAGADO"),
                dto.getIdPedido()
        );

        return new PagoDTO(pagoGuardado.getPedidoId(), pagoGuardado.getMonto().doubleValue());
    }
}
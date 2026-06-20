package com.shopconnect.ms_pagos.service;

import com.shopconnect.ms_pagos.Dto.PagoDTO;
import com.shopconnect.ms_pagos.model.Pago;
import com.shopconnect.ms_pagos.repository.pagosRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class pagosService {

    private final pagosRepository repository;

    // Inyección por constructor obligatoria para la rúbrica
    public pagosService(pagosRepository repository) {
        this.repository = repository;
    }

    // 1. Obtener todos los pagos reales de la base de datos Oracle
    public List<PagoDTO> obtenerPagosReales() {
        return repository.findAll()
                .stream()
                .map(pago -> new PagoDTO(pago.getPedidoId(), pago.getMonto().doubleValue()))
                .collect(Collectors.toList());
    }

    // 2. Procesar y guardar un pago real con reglas de negocio
    public PagoDTO guardarPagoReal(PagoDTO dto) {
        // Regla de negocio básica: El pago debe tener un monto válido
        if (dto.getMonto() <= 0) {
            throw new RuntimeException("El monto del pago debe ser mayor a cero");
        }

        Pago nuevoPago = new Pago();
        nuevoPago.setPedidoId(dto.getIdPedido());
        // Convertimos el Double del DTO al BigDecimal que exige tu entidad Pago
        nuevoPago.setMonto(BigDecimal.valueOf(dto.getMonto())); 

        // Guardamos en la base de datos Oracle
        Pago pagoGuardado = repository.save(nuevoPago);

        // Retornamos el DTO de confirmación segura
        return new PagoDTO(pagoGuardado.getPedidoId(), pagoGuardado.getMonto().doubleValue());
    }
}
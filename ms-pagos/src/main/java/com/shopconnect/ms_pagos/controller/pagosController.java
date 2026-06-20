package com.shopconnect.ms_pagos.controller;

import com.shopconnect.ms_pagos.Dto.PagoDTO;
import com.shopconnect.ms_pagos.service.pagosService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/pagos")
public class pagosController {

    private final pagosService service;

    // Inyección por constructor
    public pagosController(pagosService service) {
        this.service = service;
    }

    // GET: Consultar el historial de pagos reales de Oracle
    @GetMapping
    public List<PagoDTO> obtenerTodosLosPagos() {
        return service.obtenerPagosReales();
    }

    // POST: Recibir un pago en JSON desde el cliente y procesarlo de verdad
    @PostMapping
    public PagoDTO procesarPago(@RequestBody PagoDTO nuevoPagoDTO) {
        return service.guardarPagoReal(nuevoPagoDTO);
    }
}
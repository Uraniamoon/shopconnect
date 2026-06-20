package com.shopconnect.ms_pedidos.controller;

import com.shopconnect.ms_pedidos.Dto.PedidoDTO;
import com.shopconnect.ms_pedidos.service.pedidosService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final pedidosService service;

    // Inyección por constructor de la capa de lógica
    public PedidoController(pedidosService service) {
        this.service = service;
    }

    // GET: Retorna todos los pedidos reales consultando al Service
    @GetMapping
    public List<PedidoDTO> obtenerTodosLosPedidos() {
        return service.obtenerPedidosReales();
    }

    // POST: Recibe el DTO desde Postman y gatilla la creación real en base de datos
    @PostMapping
    public PedidoDTO crearPedido(@RequestBody PedidoDTO nuevoPedidoDTO) {
        return service.guardarPedidoReal(nuevoPedidoDTO);
    }
}
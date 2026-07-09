package com.shopconnect.ms_pedidos.controller;

import com.shopconnect.ms_pedidos.dto.PedidoDTO;
import com.shopconnect.ms_pedidos.dto.ErrorResponseDTO;
import com.shopconnect.ms_pedidos.dto.EstadoUpdateDTO;
import com.shopconnect.ms_pedidos.service.pedidosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@Tag(name = "Pedidos", description = "Operaciones de gestion de pedidos")
public class PedidoController {

    private final pedidosService service;

    public PedidoController(pedidosService service) {
        this.service = service;
    }

    @Operation(summary = "Listar todos los pedidos",
            description = "Devuelve una lista con todos los pedidos registrados.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de pedidos devuelta correctamente",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PedidoDTO.class)))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping
    public List<PedidoDTO> obtenerTodosLosPedidos() {
        return service.obtenerPedidosReales();
    }

    @Operation(summary = "Buscar pedido por ID",
            description = "Busca un pedido por su identificador unico.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedido encontrado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PedidoDTO.class))),
            @ApiResponse(responseCode = "400", description = "Pedido no encontrado con el ID indicado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping("/{id}")
    public PedidoDTO obtenerPedidoPorId(
            @Parameter(description = "Identificador unico del pedido", example = "1", required = true)
            @PathVariable Long id) {
        return service.obtenerPedidoPorId(id);
    }

    @Operation(summary = "Crear un nuevo pedido",
            description = "Crea un pedido con el monto total.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Pedido creado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PedidoDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos invalidos",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoDTO crearPedido(@Valid @RequestBody PedidoDTO nuevoPedidoDTO) {
        return service.guardarPedidoReal(nuevoPedidoDTO);
    }

    @Operation(summary = "Actualizar estado del pedido",
            description = "Actualiza el estado de un pedido (usado internamente por ms-pagos).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estado actualizado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PedidoDTO.class))),
            @ApiResponse(responseCode = "400", description = "Pedido no encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PutMapping("/{id}/estado")
    public void actualizarEstado(
            @Parameter(description = "Identificador unico del pedido", example = "1", required = true)
            @PathVariable Long id,
            @Valid @RequestBody EstadoUpdateDTO estadoDTO) {
        service.actualizarEstado(id, estadoDTO.getEstado());
    }

    @Operation(summary = "Eliminar un pedido por ID",
            description = "Elimina permanentemente un pedido de la base de datos.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Pedido eliminado correctamente", content = @Content),
            @ApiResponse(responseCode = "400", description = "Pedido no encontrado con el ID indicado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarPedido(
            @Parameter(description = "Identificador unico del pedido", example = "1", required = true)
            @PathVariable Long id) {
        service.eliminarPedido(id);
    }
}

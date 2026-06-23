package com.shopconnect.ms_inventario.controller;

import com.shopconnect.ms_inventario.dto.InventarioDTO;
import com.shopconnect.ms_inventario.dto.ErrorResponseDTO;
import com.shopconnect.ms_inventario.service.inventarioService;
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
@RequestMapping("/api/inventario")
@Tag(name = "Inventario", description = "Operaciones de gestion de inventario")
public class inventarioController {

    private final inventarioService service;

    public inventarioController(inventarioService service) {
        this.service = service;
    }

    @Operation(summary = "Listar todo el inventario",
            description = "Devuelve una lista con todas las existencias de inventario.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de inventario devuelta correctamente",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = InventarioDTO.class)))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping
    public List<InventarioDTO> obtenerInventario() {
        return service.obtenerInventarioReal();
    }

    @Operation(summary = "Buscar inventario por ID",
            description = "Busca un registro de inventario por su identificador unico.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Inventario encontrado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = InventarioDTO.class))),
            @ApiResponse(responseCode = "400", description = "Inventario no encontrado con el ID indicado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping("/{id}")
    public InventarioDTO obtenerInventarioPorId(
            @Parameter(description = "Identificador unico del inventario", example = "1", required = true)
            @PathVariable Long id) {
        return service.obtenerInventarioPorId(id);
    }

    @Operation(summary = "Agregar o actualizar stock",
            description = "Guarda o actualiza un registro de inventario en la base de datos.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Stock guardado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = InventarioDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos invalidos",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InventarioDTO agregarStock(@Valid @RequestBody InventarioDTO nuevoInventarioDTO) {
        return service.guardarStockReal(nuevoInventarioDTO);
    }

    @Operation(summary = "Eliminar inventario por ID",
            description = "Elimina permanentemente un registro de inventario.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Inventario eliminado correctamente", content = @Content),
            @ApiResponse(responseCode = "400", description = "Inventario no encontrado con el ID indicado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarInventario(
            @Parameter(description = "Identificador unico del inventario", example = "1", required = true)
            @PathVariable Long id) {
        service.eliminarInventario(id);
    }
}

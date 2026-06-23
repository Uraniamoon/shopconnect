package com.shopconnect.ms_pagos.controller;

import com.shopconnect.ms_pagos.dto.PagoDTO;
import com.shopconnect.ms_pagos.dto.ErrorResponseDTO;
import com.shopconnect.ms_pagos.service.pagosService;
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
@RequestMapping("/api/pagos")
@Tag(name = "Pagos", description = "Operaciones de procesamiento de pagos")
public class pagosController {

    private final pagosService service;

    public pagosController(pagosService service) {
        this.service = service;
    }

    @Operation(summary = "Listar todos los pagos",
            description = "Devuelve una lista con todos los pagos registrados.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de pagos devuelta correctamente",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PagoDTO.class)))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping
    public List<PagoDTO> obtenerTodosLosPagos() {
        return service.obtenerPagosReales();
    }

    @Operation(summary = "Buscar pago por ID",
            description = "Busca un pago por su identificador unico.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pago encontrado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PagoDTO.class))),
            @ApiResponse(responseCode = "400", description = "Pago no encontrado con el ID indicado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping("/{id}")
    public PagoDTO obtenerPagoPorId(
            @Parameter(description = "Identificador unico del pago", example = "1", required = true)
            @PathVariable Long id) {
        return service.obtenerPagoPorId(id);
    }

    @Operation(summary = "Procesar un nuevo pago",
            description = "Procesa y guarda un pago en la base de datos.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Pago procesado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PagoDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos invalidos",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PagoDTO procesarPago(@Valid @RequestBody PagoDTO nuevoPagoDTO) {
        return service.guardarPagoReal(nuevoPagoDTO);
    }

    @Operation(summary = "Eliminar un pago por ID",
            description = "Elimina permanentemente un pago de la base de datos.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Pago eliminado correctamente", content = @Content),
            @ApiResponse(responseCode = "400", description = "Pago no encontrado con el ID indicado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarPago(
            @Parameter(description = "Identificador unico del pago", example = "1", required = true)
            @PathVariable Long id) {
        service.eliminarPago(id);
    }
}

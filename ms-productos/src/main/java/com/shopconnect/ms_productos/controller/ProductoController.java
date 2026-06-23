package com.shopconnect.ms_productos.controller;

import com.shopconnect.ms_productos.dto.ProductoDTO;
import com.shopconnect.ms_productos.dto.ErrorResponseDTO;
import com.shopconnect.ms_productos.service.productosService;
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
@RequestMapping("/api/productos")
@Tag(name = "Productos", description = "Operaciones de gestion de productos")
public class ProductoController {

    private final productosService service;

    public ProductoController(productosService service) {
        this.service = service;
    }

    @Operation(summary = "Listar todos los productos",
            description = "Devuelve una lista con todos los productos del catalogo.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de productos devuelta correctamente",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ProductoDTO.class)))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping
    public List<ProductoDTO> obtenerCatalogo() {
        return service.obtenerProductosReales();
    }

    @Operation(summary = "Buscar producto por ID",
            description = "Busca un producto por su identificador unico.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto encontrado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductoDTO.class))),
            @ApiResponse(responseCode = "400", description = "Producto no encontrado con el ID indicado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping("/{id}")
    public ProductoDTO obtenerProductoPorId(
            @Parameter(description = "Identificador unico del producto", example = "1", required = true)
            @PathVariable Long id) {
        return service.obtenerProductoPorId(id);
    }

    @Operation(summary = "Crear un nuevo producto",
            description = "Crea un producto con nombre y precio de venta.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Producto creado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductoDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos invalidos",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductoDTO agregarProducto(@Valid @RequestBody ProductoDTO nuevoProductoDTO) {
        return service.guardarProductoReal(nuevoProductoDTO);
    }

    @Operation(summary = "Eliminar un producto por ID",
            description = "Elimina permanentemente un producto de la base de datos.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Producto eliminado correctamente", content = @Content),
            @ApiResponse(responseCode = "400", description = "Producto no encontrado con el ID indicado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarProducto(
            @Parameter(description = "Identificador unico del producto", example = "1", required = true)
            @PathVariable Long id) {
        service.eliminarProducto(id);
    }
}

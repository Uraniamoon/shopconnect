package com.shopconnect.ms_usuarios.controller;

import com.shopconnect.ms_usuarios.dto.UsuarioDTO;
import com.shopconnect.ms_usuarios.dto.ErrorResponseDTO;
import com.shopconnect.ms_usuarios.service.usuariosService;
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
@RequestMapping("/api/usuarios")
@Tag(name = "Usuarios", description = "Operaciones de gestion de usuarios")
public class UsuarioController {

    private final usuariosService service;

    public UsuarioController(usuariosService service) {
        this.service = service;
    }

    @Operation(summary = "Listar todos los usuarios",
            description = "Devuelve una lista con todos los usuarios registrados. Cada elemento se responde como UsuarioDTO.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de usuarios devuelta correctamente",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UsuarioDTO.class)))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping
    public List<UsuarioDTO> obtenerUsuarios() {
        return service.obtenerUsuariosReales();
    }

    @Operation(summary = "Buscar usuario por ID",
            description = "Busca un usuario por su identificador unico.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario encontrado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioDTO.class))),
            @ApiResponse(responseCode = "400", description = "Usuario no encontrado con el ID indicado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping("/{id}")
    public UsuarioDTO obtenerUsuarioPorId(
            @Parameter(description = "Identificador unico del usuario", example = "1", required = true)
            @PathVariable Long id) {
        return service.obtenerUsuarioPorId(id);
    }

    @Operation(summary = "Crear un nuevo usuario",
            description = "Crea un usuario con nombre y correo electronico.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos invalidos",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioDTO registrarUsuario(@Valid @RequestBody UsuarioDTO nuevoUsuarioDTO) {
        return service.guardarUsuarioReal(nuevoUsuarioDTO);
    }

    @Operation(summary = "Eliminar un usuario por ID",
            description = "Elimina permanentemente un usuario de la base de datos.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuario eliminado correctamente", content = @Content),
            @ApiResponse(responseCode = "400", description = "Usuario no encontrado con el ID indicado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarUsuario(
            @Parameter(description = "Identificador unico del usuario", example = "1", required = true)
            @PathVariable Long id) {
        service.eliminarUsuario(id);
    }
}

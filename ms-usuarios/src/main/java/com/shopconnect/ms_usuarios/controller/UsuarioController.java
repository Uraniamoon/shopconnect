package com.shopconnect.ms_usuarios.controller;

import com.shopconnect.ms_usuarios.Dto.UsuarioDTO;
import com.shopconnect.ms_usuarios.service.usuariosService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final usuariosService service;

    // Inyección por constructor de la capa de lógica (Service)
    public UsuarioController(usuariosService service) {
        this.service = service;
    }

    // GET: Retorna la lista de usuarios ocultando las contraseñas
    @GetMapping
    public List<UsuarioDTO> obtenerUsuarios() {
        return service.obtenerUsuariosReales();
    }

    // POST: Recibe los datos limpios desde Postman para registrar un usuario real
    @PostMapping
    public UsuarioDTO registrarUsuario(@RequestBody UsuarioDTO nuevoUsuarioDTO) {
        return service.guardarUsuarioReal(nuevoUsuarioDTO);
    }
}
package com.shopconnect.ms_usuarios.service;

import com.shopconnect.ms_usuarios.Dto.UsuarioDTO;
import com.shopconnect.ms_usuarios.model.usuario;
import com.shopconnect.ms_usuarios.repository.usuariosRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class usuariosService {

    private final usuariosRepository repository;

    // Inyección por constructor (Obligatoria para cumplir la pauta de buenas prácticas)
    public usuariosService(usuariosRepository repository) {
        this.repository = repository;
    }

    // 1. Obtener todos los usuarios reales desde Oracle XE transformados a DTO seguro
    public List<UsuarioDTO> obtenerUsuariosReales() {
        return repository.findAll()
                .stream()
                .map(user -> new UsuarioDTO(
                        user.getIdUsuario(), 
                        user.getNombre(), 
                        user.getEmail()
                ))
                .collect(Collectors.toList());
    }

    // 2. Registrar y guardar un usuario nuevo de forma real en la base de datos
    public UsuarioDTO guardarUsuarioReal(UsuarioDTO dto) {
        // Regla de negocio elemental: El correo electrónico no puede venir vacío
        if (dto.getCorreoElectronico() == null || dto.getCorreoElectronico().trim().isEmpty()) {
            throw new RuntimeException("El correo electrónico del usuario es obligatorio");
        }

        usuario nuevoUsuario = new usuario();
        nuevoUsuario.setNombre(dto.getNombre());
        nuevoUsuario.setEmail(dto.getCorreoElectronico());
        nuevoUsuario.setPassword("ClaveSimulada123"); // Le pones una clave por defecto para cumplir con el @NotNull de tu entidad

        // Persistencia real en Oracle XE a través de JPA
        usuario guardado = repository.save(nuevoUsuario);

        // Devolvemos el DTO de confirmación blindado (sin password)
        return new UsuarioDTO(
                guardado.getIdUsuario(), 
                guardado.getNombre(), 
                guardado.getEmail()
        );
    }
}
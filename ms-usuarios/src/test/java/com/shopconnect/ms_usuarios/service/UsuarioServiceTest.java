package com.shopconnect.ms_usuarios.service;

import com.shopconnect.ms_usuarios.dto.UsuarioDTO;
import com.shopconnect.ms_usuarios.model.usuario;
import com.shopconnect.ms_usuarios.repository.usuariosRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private usuariosRepository repository;

    @InjectMocks
    private usuariosService service;

    @Test
    void crearDebeGuardarUsuario() {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setNombre("Juan");
        dto.setCorreoElectronico("juan@mail.com");
        when(repository.save(any(usuario.class))).thenReturn(new usuario());
        UsuarioDTO resultado = service.guardarUsuarioReal(dto);
        assertNotNull(resultado);
        verify(repository).save(any(usuario.class));
    }

    @Test
    void listarDebeRetornarUsuarios() {
        when(repository.findAll()).thenReturn(List.of(new usuario()));
        assertEquals(1, service.obtenerUsuariosReales().size());
    }

    @Test
    void buscarPorIdDebeRetornarUsuario() {
        usuario u = new usuario();
        u.setIdUsuario(1L);
        u.setNombre("Ana");
        when(repository.findById(1L)).thenReturn(Optional.of(u));
        assertEquals("Ana", service.obtenerUsuarioPorId(1L).getNombre());
    }

    @Test
    void eliminarDebeLlamarDelete() {
        when(repository.existsById(1L)).thenReturn(true);
        service.eliminarUsuario(1L);
        verify(repository).deleteById(1L);
    }
}

package com.shopconnect.ms_inventario.service;

import com.shopconnect.ms_inventario.dto.InventarioDTO;
import com.shopconnect.ms_inventario.model.Inventario;
import com.shopconnect.ms_inventario.repository.inventarioRepository;
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
class InventarioServiceTest {

    @Mock
    private inventarioRepository repository;

    @InjectMocks
    private inventarioService service;

    @Test
    void crearDebeGuardarInventario() {
        InventarioDTO dto = new InventarioDTO();
        dto.setStockActual(50);
        Inventario i = new Inventario();
        i.setIdInventario(1L);
        when(repository.save(any(Inventario.class))).thenReturn(i);
        InventarioDTO resultado = service.guardarStockReal(dto);
        assertNotNull(resultado);
        verify(repository).save(any(Inventario.class));
    }

    @Test
    void listarDebeRetornarInventarios() {
        when(repository.findAll()).thenReturn(List.of(new Inventario()));
        assertEquals(1, service.obtenerInventarioReal().size());
    }

    @Test
    void buscarPorIdDebeRetornarInventario() {
        Inventario i = new Inventario();
        i.setIdInventario(1L);
        i.setStockActual(50);
        when(repository.findById(1L)).thenReturn(Optional.of(i));
        assertEquals(50, service.obtenerInventarioPorId(1L).getStockActual());
    }

    @Test
    void eliminarDebeLlamarDelete() {
        when(repository.existsById(1L)).thenReturn(true);
        service.eliminarInventario(1L);
        verify(repository).deleteById(1L);
    }
}

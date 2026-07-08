package com.shopconnect.ms_productos.service;

import com.shopconnect.ms_productos.dto.ProductoDTO;
import com.shopconnect.ms_productos.model.Producto;
import com.shopconnect.ms_productos.repository.productosRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private productosRepository repository;

    @InjectMocks
    private productosService service;

    @Test
    void crearDebeGuardarProducto() {
        ProductoDTO dto = new ProductoDTO();
        dto.setNombre("Laptop");
        dto.setPrecioVenta(999.99);
        Producto p = new Producto();
        p.setId(1L);
        p.setNombre("Laptop");
        p.setPrecio(BigDecimal.valueOf(999.99));
        when(repository.save(any(Producto.class))).thenReturn(p);
        ProductoDTO resultado = service.guardarProductoReal(dto);
        assertNotNull(resultado);
        verify(repository).save(any(Producto.class));
    }

    @Test
    void listarDebeRetornarProductos() {
        Producto p = new Producto();
        p.setPrecio(BigDecimal.valueOf(999.99));
        when(repository.findAll()).thenReturn(List.of(p));
        assertEquals(1, service.obtenerProductosReales().size());
    }

    @Test
    void buscarPorIdDebeRetornarProducto() {
        Producto p = new Producto();
        p.setId(1L);
        p.setNombre("Laptop");
        p.setPrecio(BigDecimal.valueOf(999.99));
        when(repository.findById(1L)).thenReturn(Optional.of(p));
        assertEquals("Laptop", service.obtenerProductoPorId(1L).getNombre());
    }

    @Test
    void eliminarDebeLlamarDelete() {
        when(repository.existsById(1L)).thenReturn(true);
        service.eliminarProducto(1L);
        verify(repository).deleteById(1L);
    }
}

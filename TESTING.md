# Testing - ShopConnect Microservices

## Resumen

5 microservicios, 20 tests unitarios JUnit 5 + Mockito, mas integracion HTTP con RestTemplate.

| Modulo | Archivo | Tests |
|--------|---------|-------|
| `ms-usuarios` | `UsuarioServiceTest.java` | 4 |
| `ms-productos` | `ProductoServiceTest.java` | 4 |
| `ms-inventario` | `InventarioServiceTest.java` | 4 |
| `ms-pagos` | `PagoServiceTest.java` | 4 (incluye mock de RestTemplate) |
| `ms-pedidos` | `PedidoServiceTest.java` | 4 |

## Ejecutar tests

### Con Docker (recomendado)

Desde la raiz del proyecto:

```bash
# Un modulo
docker run --rm -v "$(pwd):/app" -w /app maven:3.9-eclipse-temurin-17 bash -c "cd ms-usuarios && mvn test"

# Todos los modulos en paralelo
docker run --rm -v "$(pwd):/app" -w /app maven:3.9-eclipse-temurin-17 bash -c \
  "cd ms-usuarios && mvn test -q && cd ../ms-productos && mvn test -q && cd ../ms-inventario && mvn test -q && cd ../ms-pagos && mvn test -q && cd ../ms-pedidos && mvn test -q && echo 'TODOS OK'"
```

### Sin Docker (requiere JDK 17+ local)

```bash
cd ms-usuarios && mvn test
```

## Que prueba cada test

Cada `*ServiceTest` cubre 4 operaciones CRUD del servicio correspondiente:

| Test | Que verifica |
|------|-------------|
| `crearDebeGuardar*` | `service.guardar*Real(dto)` retorna un DTO no nulo y llama a `repository.save()` |
| `listarDebeRetornar*` | `service.obtener*Reales()` retorna lista con elementos |
| `buscarPorIdDebeRetornar*` | `service.obtener*PorId(id)` retorna un DTO no nulo |
| `eliminarDebeLlamarDelete` | `service.eliminar*(id)` llama a `repository.deleteById()` |

## Arquitectura de tests

- `@ExtendWith(MockitoExtension.class)` — NO usa `@SpringBootTest` para evitar dependencia de Oracle DB.
- Todos los repositorios son `@Mock`, los servicios son `@InjectMocks`.
- `maven-surefire-plugin` configurado con `-Dnet.bytebuddy.experimental=true` para compatibilidad con JDK 17.
- Tests auto-generados `*ApplicationTests.java` estan excluidos del plugin (fallan sin DB).

## Comunicacion HTTP entre servicios (RestTemplate)

- `ms-pagos` consume `GET /api/pedidos/{id}` de `ms-pedidos` para validar que el pedido existe antes de procesar el pago.
- `ms-pagos` consume `PUT /api/pedidos/{id}/estado` de `ms-pedidos` para actualizar el estado a "PAGADO" tras un pago exitoso.
- Los tests de `PagoServiceTest` usan `@Mock RestTemplate` y configuran `doReturn/when` para simular las respuestas HTTP sin necesidad de que los servicios esten levantados.

## Notas

- Los servicios convierten `BigDecimal` de las entidades a `Double` de los DTOs. Los mocks deben inicializar los campos BigDecimal (ej: `p.setPrecio(BigDecimal.valueOf(999.99))`).
- `Producto` usa `setPrecio(BigDecimal)` y `getPrecio()`.
- `Pago` usa `setMonto(BigDecimal)` y `getMonto()`.
- `Pedido` usa `setTotal(BigDecimal)` y `getTotal()`.

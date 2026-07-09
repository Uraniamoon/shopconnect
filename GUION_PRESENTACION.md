# Guion de Presentacion — ShopConnect (10 minutos)

**3 Integrantes** | Nivel: Mixto (funcional + tecnico)

---

## Parte 1 — Introduccion y Arquitectura (0:00 – 2:00) — Persona 1

"Hola, bienvenidos a ShopConnect, un sistema backend de comercio electronico con **microservicios en Java y Spring Boot**.

La arquitectura tiene **5 microservicios independientes**: usuarios, productos, inventario, pagos y pedidos, todos conectados a una base **Oracle XE** compartida y orquestados con **Docker Compose**.

Usamos Java 17, Spring Boot 3.4.4, JPA/Hibernate para el mapeo relacional, Swagger para documentacion interactiva y **RestTemplate** para la comunicacion HTTP entre servicios."

---

## Parte 2 — Microservicios: Usuarios, Productos, Inventario (2:00 – 4:00) — Persona 2

"**ms-usuarios**: CRUD de usuarios con relaciones JPA a RolUsuario y Direccion. Usamos DTOs para separar persistencia de presentacion.

**ms-productos**: catalogo con nombre, precio (BigDecimal en BD, Double en DTO), sku y stock.

**ms-inventario**: control de stock con CRUD.

Los 3 implementan `@RestControllerAdvice` con `ApiExceptionHandler` que captura errores de validacion y runtime, retornando `ErrorResponseDTO` con mensajes claros.

Cada modulo expone Swagger en `/swagger-ui/index.html` y `/v3/api-docs`."

---

## Parte 3 — Microservicios: Pagos, Pedidos y Comunicacion HTTP (4:00 – 6:30) — Persona 3

"**ms-pagos**: procesa pagos asociados a pedidos con regla de negocio: monto > 0.

**ms-pedidos**: crea pedidos con fecha automatica, total BigDecimal, ID de usuario y relacion con estado.

Lo novedoso aqui es la **comunicacion HTTP entre servicios usando RestTemplate**:

1. Cuando `ms-pagos` recibe un pago, primero valida via HTTP que el pedido exista en `ms-pedidos`: `GET /api/pedidos/{id}`
2. Si el pedido existe, procesa el pago en base de datos
3. Luego notifica a `ms-pedidos` via `PUT /api/pedidos/{id}/estado` para actualizar el estado del pedido a 'PAGADO'

Esto reemplaza la logica anterior que solo trabajaba con IDs sin validacion, y demuestra como los microservicios pueden colaborar via HTTP sin depender exclusivamente de la base de datos compartida."

---

## Parte 4 — Testing (6:30 – 8:00) — Persona 2 o 3

"Implementamos **20 tests unitarios** con JUnit 5 + Mockito, 4 por modulo (crear, listar, buscarPorId, eliminar).

Usamos `@ExtendWith(MockitoExtension.class)` en vez de `@SpringBootTest` para evitar dependencia de Oracle. Los repositorios se mockean con `@Mock` y los servicios con `@InjectMocks`.

Para los tests de `ms-pagos`, tambien mockeamos `RestTemplate` con `@Mock` para simular las respuestas HTTP sin necesidad de que los otros servicios esten corriendo.

Configuramos el plugin `maven-surefire-plugin` para excluir los tests de contexto (`*ApplicationTests.java`) que si requieren base de datos.

Ejecucion con Docker:
```
docker run --rm -v "$(pwd):/app" maven:3.9-eclipse-temurin-17 bash -c "cd ms-usuarios && mvn test"
```"

---

## Parte 5 — Despliegue con Docker y Cierre (8:00 – 10:00)

**Persona 1**: "Con `docker-compose up --build` se levantan 5 contenedores (puertos 8081 al 8085) + Oracle XE (1521) + volumen persistente. Cada microservicio expone su API y su Swagger UI."

**Persona 2**: "Aprendizajes: arquitectura de microservicios con Spring Boot y Docker, JPA/Hibernate, documentacion con Swagger, manejo de errores centralizado, testing con Mockito y comunicacion HTTP con RestTemplate."

**Persona 3**: "Trabajo futuro: API Gateway con Spring Cloud, autenticacion JWT, autodescubrimiento con Eureka, frontend web o mobile. Muchas gracias por su atencion. ¿Alguna pregunta?"

---

## Notas Tecnicas

- Puertos: usuarios=8081, productos=8082, inventario=8083, pagos=8084, pedidos=8085
- Oracle XE: system/admin@XE
- Swagger: `http://localhost:{puerto}/swagger-ui/index.html`
- RestTemplate configurado en `RestTemplateConfig.java` (config package)
- Comunicacion: `ms-pagos` -> `ms-pedidos` via `GET /api/pedidos/{id}` y `PUT /api/pedidos/{id}/estado`
- Repositorio: `github.com/Uraniamoon/shopconnect`, rama `feat/urania`

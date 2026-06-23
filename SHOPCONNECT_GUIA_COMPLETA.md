# ShopConnect — Guia completa de implementacion

## 1. Correccion de POMs

| Problema | Solucion |
|----------|----------|
| Spring Boot `4.0.6` (inexistente) | Cambiar a `3.4.4` en todos los `pom.xml` |
| `spring-boot-starter-webmvc` no existe | Reemplazar por `spring-boot-starter-web` |
| Starters de test invalidos (`spring-boot-starter-validation`, `spring-boot-starter-web`, `spring-boot-starter-webmvc`) en scope test | Reemplazar por `spring-boot-starter-test` |
| `maven-compiler-plugin` redundante con configuracion parcial | Eliminar (Spring Boot parent ya lo provee) |
| XML corrupto en `ms-inventario/pom.xml` (dependencies truncado/duplicado) | Reconstruir el bloque completo de dependencias |

## 2. Dependencia Swagger (springdoc-openapi)

Agregar en `pom.xml` de ms-usuarios, ms-productos, ms-pagos, ms-pedidos (ms-inventario ya lo tiene):

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.7.0</version>
</dependency>

> **Version 2.6.0** causa `NoSuchMethodError: ControllerAdviceBean.<init>(Object)` con
> Spring Boot 3.4.4 / Spring Framework 6.2. Usar **2.7.0** o superior.
```

## 3. OpenApiConfig.java

Crear en `com.shopconnect.ms_*/config/OpenApiConfig.java` con:

```java
@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("ShopConnect - <NombreMicroservicio>")
                .version("1.0")
                .description("API para gestion de <recurso>"))
            .addServersItem(new Server()
                .url("http://localhost:<puerto>")
                .description("Servidor local"));
    }
}
```

| Microservicio | Puerto |
|--------------|-------|
| ms-usuarios | 8081 |
| ms-productos | 8082 |
| ms-inventario | 8083 |
| ms-pagos | 8084 |
| ms-pedidos | 8085 |

## 4. ErrorResponseDTO.java

Crear en `com.shopconnect.ms_*/dto/ErrorResponseDTO.java`:

```java
@Schema(description = "Estructura estandar de respuesta de error")
public class ErrorResponseDTO {
    @Schema(description = "Mensaje descriptivo del error", example = "El campo nombre es obligatorio")
    private String mensaje;

    @Schema(description = "Fecha y hora en que ocurrio el error")
    private LocalDateTime fechaHora;
}
```

## 5. ApiExceptionHandler.java

Crear en `com.shopconnect.ms_*/exception/ApiExceptionHandler.java`:

```java
@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidation(MethodArgumentNotValidException ex) { ... }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseDTO> handleRuntime(RuntimeException ex) { ... }
}
```

## 6. Anotaciones Swagger en Controllers

- `@Tag(name = "X", description = "Y")` a nivel de clase
- `@Operation(summary = "...", description = "...")` en cada metodo
- `@ApiResponses(...)` con `@ApiResponse` para 200, 400, 404, 500
- `@Parameter(description = "...", example = "...")` en parametros `@PathVariable`
- `@Valid` en cuerpos POST

## 7. Metodos faltantes en Controllers y Services

Agregar para cada microservicio:

### Controller
```java
@GetMapping("/{id}")
public ResponseEntity<XDTO> obtenerPorId(@PathVariable Long id) { ... }

@DeleteMapping("/{id}")
public ResponseEntity<Void> eliminar(@PathVariable Long id) { ... }
```

### Service
```java
public XDTO findById(Long id) { ... }
public void delete(Long id) { ... }
```

## 8. Package naming: `Dto` → `dto` (lowercase)

El proyecto original usaba `package com.shopconnect.ms_*.**Dto**;` (con `D` mayuscula).
Se cambio a `package com.shopconnect.ms_*.**dto**;` (minuscula) para evitar problemas
de case-sensitivity al compilar en Linux (Docker) desde Windows.

**Cambios realizados:**
- Renombrar directorio `Dto/` → `dto/` en los 5 modulos
- Actualizar `package` en todos los DTOs y ErrorResponseDTO
- Actualizar `import` en todos los Controllers y Services

> **Windows:** Si VSCode muestra errores de `import com.shopconnect.ms_*.dto cannot be resolved`,
> limpia el cache de JDTLS: `Ctrl+Shift+P` → `Java: Clean the Java language server workspace`
> → `Reload and delete`.

## 9. Anotaciones @Schema en DTOs

Cada campo de los DTOs existentes debe tener:
- `@Schema(description = "...", example = "...")`

Ejemplo:
```java
@Schema(description = "Nombre completo del usuario", example = "Juan Perez")
private String nombre;
```

## 10. Correccion de columnas NOT NULL en entidades

Al hacer POST para crear registros, varios microservicios fallaban con
`ORA-01400: cannot insert NULL` porque las entidades tenian `@JoinColumn(nullable = false)`
en relaciones que el DTO no incluia y el servicio no asignaba.

| Microservicio | Columna NOT NULL | Solucion |
|---------------|-----------------|----------|
| ms-usuarios | `id_direccion`, `id_rol_usuario` | Remover `nullable = false` en `usuario.java` |
| ms-productos | `id_categoria`, `id_marca` | Remover `nullable = false` en `Producto.java` |
| ms-pagos | `id_metodo_pago` | Remover `nullable = false` en `Pago.java` |
| ms-pedidos | `id_estado_pedido` | Remover `nullable = false` en `Pedido.java` |

Ademas, `ms-productos` tenia campos `sku` y `stock` con `@NotNull` en la entidad
que no se asignaban en el service. Se agregaron valores default en `productosService.java`:
```java
nuevoProd.setSku("SKU-" + System.currentTimeMillis());
nuevoProd.setStock(1);
```

Tambien se corrigio `getId_producto()` en `Producto.java` que lanzaba
`UnsupportedOperationException` en vez de retornar el ID.

**Actualizar la base de datos existente** (las tablas ya creadas con NOT NULL):
```powershell
$sql = @"
ALTER TABLE USUARIOS MODIFY (ID_DIRECCION NUMBER NULL);
ALTER TABLE USUARIOS MODIFY (ID_ROL_USUARIO NUMBER NULL);
ALTER TABLE PRODUCTO MODIFY (ID_CATEGORIA NUMBER NULL);
ALTER TABLE PRODUCTO MODIFY (ID_MARCA NUMBER NULL);
ALTER TABLE PAGO MODIFY (ID_METODO_PAGO NUMBER NULL);
ALTER TABLE PEDIDO MODIFY (ID_ESTADO_PEDIDO NUMBER NULL);
"@
$sql | docker exec -i oracle-db sqlplus -s system/admin@XE
```

## 11. docker-compose.yml

- Cambiar `version: '3.8'` → eliminar linea (obsoleto)
- Agregar `restart: unless-stopped` a cada microservicio

## 12. Construir y desplegar

```powershell
# 1. Forzar limpieza si hay error de lock
Get-ChildItem -Path . -Directory -Filter "target" | Remove-Item -Recurse -Force

# 2. Compilar con Docker Maven (desde la carpeta raiz shopconnect)
docker run --rm -v "$(pwd):/app" -w /app maven:3.9-eclipse-temurin-17 bash -c "cd ms-usuarios && mvn clean package -DskipTests && cd ../ms-productos && mvn clean package -DskipTests && cd ../ms-inventario && mvn clean package -DskipTests && cd ../ms-pagos && mvn clean package -DskipTests && cd ../ms-pedidos && mvn clean package -DskipTests"

# 3. Construir imagenes Docker y levantar
docker compose up -d --build

# 4. Verificar contenedores
docker compose ps

# 5. Ver logs si algo falla
docker compose logs -f <nombre-servicio>
```

> Si tienes Maven local, puedes usar `mvn clean package -DskipTests` directamente.

## 13. URLs Swagger UI

| Microservicio | Swagger UI |
|--------------|------------|
| ms-usuarios | http://localhost:8081/swagger-ui/index.html |
| ms-productos | http://localhost:8082/swagger-ui/index.html |
| ms-inventario | http://localhost:8083/swagger-ui/index.html |
| ms-pagos | http://localhost:8084/swagger-ui/index.html |
| ms-pedidos | http://localhost:8085/swagger-ui/index.html |

## 14. Resolucion de problemas comunes

- **Error: `Ports are not available`** → Verificar que ningun otro proceso use el puerto (`netstat -ano | findstr :PUERTO`)
- **Error: `OCI runtime create failed`** → Detener contenedores previos (`docker compose down`) y reintentar
- **Error: `IO error` en compilacion** → Cerrar IDE o eliminar `target/` manualmente (`Remove-Item -Recurse -Force target`)
- **Microservicio crashea al iniciar** → Esperar a que Oracle DB este lista; `restart: unless-stopped` reintenta automaticamente
- **Error: `Cannot determine embedded database driver class`** → Verificar que `application.properties` tenga las propiedades de Oracle (no H2)

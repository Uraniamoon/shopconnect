# Corrección de errores en POM.xml - ShopConnect

## Resumen de errores encontrados

Se detectaron **5 problemas críticos** en los archivos `pom.xml` y **1 problema de configuración** en los puertos.

---

## Error #1 (CRÍTICO): Versión de Spring Boot inexistente

**Archivos:** Los 5 pom.xml
**Línea:** `<version>4.0.6</version>`

**Problema:** Spring Boot 4.x **no existe**. No ha sido lanzado por el equipo de Spring.
Maven no puede resolver el parent POM y la compilación falla inmediatamente.

**Solución:** Cambiar a `3.4.4` (última versión estable compatible con Java 17):

```xml
<version>3.4.4</version>
```

---

## Error #2 (CRÍTICO): Starter `spring-boot-starter-webmvc` no existe

**Archivos:** Los 5 pom.xml
**Línea:** `<artifactId>spring-boot-starter-webmvc</artifactId>`

**Problema:** `spring-boot-starter-webmvc` no es un starter oficial de Spring Boot.
No existe en Maven Central.

**Solución:** Reemplazar por `spring-boot-starter-web`:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

---

## Error #3 (CRÍTICO): Test starters inválidos

**Archivos:** Los 5 pom.xml

**Problema:** Estos tres starters **no existen** en Maven Central:

- `spring-boot-starter-data-jpa-test`
- `spring-boot-starter-validation-test`
- `spring-boot-starter-webmvc-test`

Además, **ninguno de los 5 POMs incluye** `spring-boot-starter-test`,
que es esencial para correr pruebas.

**Solución:** Eliminar los 3 starters inválidos y agregar el oficial:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```

---

## Error #4 (CRÍTICO): XML corrupto en `ms-inventario/pom.xml`

**Archivo:** `ms-inventario/pom.xml` (líneas 72-80)

**Problema:** Después del cierre del bloque `<dependencies>` (línea 71),
hay código XML mal formado:

```xml
</dependencies>

<dependency>                                          <!-- FUERA del bloque -->
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
<dependency>                                          <!-- Tag de cierra incorrecto -->
                                                      <!-- (debería ser </dependency>) -->
<dependency>                                          <!-- Otra vez fuera -->
    <groupId>org.spring                                <!-- TRUNCADO -->
```

Esto hace que el archivo **no sea XML válido** y Maven no pueda procesarlo.

**Solución:** Eliminar completamente las líneas 72-80 (código duplicado y roto).

---

## Error #5 (ADVERTENCIA): Bloque `maven-compiler-plugin` redundante

**Archivos:** Los 5 pom.xml

**Problema:** Hay una configuración extra del plugin `maven-compiler-plugin`
con `annotationProcessorPaths` para Lombok. Esto **no es necesario**
cuando se usa `spring-boot-starter-parent` como parent POM, ya que
Spring Boot ya lo configura automáticamente.

**Solución:** Eliminar el plugin redundante. El `spring-boot-maven-plugin`
con la exclusión de Lombok es suficiente.

---

## Error #6: Puertos desincronizados

**Problema:** Los puertos en `application.properties` no coinciden con
los mapeados en `docker-compose.yml`.

| Microservicio | application.properties | docker-compose.yml | Corrección |
|---|---|---|---|
| ms-usuarios | 8082 | 8081:8081 | → 8081 |
| ms-productos | 8081 | 8082:8082 | → 8082 |
| ms-inventario | 8085 | 8083:8083 | → 8083 |
| ms-pagos | 8084 | 8084:8084 | ✅ |
| ms-pedidos | 8083 | 8085:8085 | → 8085 |

**Solución:** Ajustar `server.port` en cada `application.properties`
para que coincida con el puerto **interno** del contenedor en Docker
(el número del lado izquierdo del mapeo `externo:interno`).

---

## Resumen de archivos modificados

| Archivo | Cambios aplicados |
|---|---|
| `ms-usuarios/pom.xml` | Versión 3.4.4, web, test, eliminar plugins extra |
| `ms-pagos/pom.xml` | Ídem |
| `ms-pedidos/pom.xml` | Ídem |
| `ms-productos/pom.xml` | Ídem |
| `ms-inventario/pom.xml` | Ídem + XML corrupto reparado |
| `ms-usuarios/application.properties` | Puerto 8081 |
| `ms-productos/application.properties` | Puerto 8082 |
| `ms-inventario/application.properties` | Puerto 8083 |
| `ms-pedidos/application.properties` | Puerto 8085 |

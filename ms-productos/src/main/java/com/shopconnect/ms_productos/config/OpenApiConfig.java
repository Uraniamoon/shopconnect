package com.shopconnect.ms_productos.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ShopConnect - Productos API")
                        .version("1.0.0")
                        .description("API REST del microservicio de Productos.\n\n"
                                + "Gestiona el catalogo de productos del sistema ShopConnect."))
                .addServersItem(new Server()
                        .url("http://localhost:8082")
                        .description("Servidor local de desarrollo"));
    }
}

package com.shopconnect.ms_usuarios.config;

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
                        .title("ShopConnect - Usuarios API")
                        .version("1.0.0")
                        .description("API REST del microservicio de Usuarios.\n\n"
                                + "Gestiona el registro de usuarios del sistema ShopConnect."))
                .addServersItem(new Server()
                        .url("http://localhost:8081")
                        .description("Servidor local de desarrollo"));
    }
}

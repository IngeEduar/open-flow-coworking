package com.nelumbo.open_flow_coworking.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl("http://localhost:8080");
        devServer.setDescription("Servidor de Desarrollo (Local)");

        Contact contact = new Contact();
        contact.setName("Eduar Avendaño");
        contact.setEmail("ingeeduar20@gmail.com");

        SecurityScheme bearerScheme = new SecurityScheme()
                .name("bearerAuth")
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");

        return new OpenAPI()
                .components(new Components().addSecuritySchemes("bearerAuth", bearerScheme))
                .info(new Info()
                        .title("Nelumbo API Documentation | Documentación de API Nelumbo")
                        .version("1.0.0")
                        .contact(contact)
                        .description("API para la gestión de accesos en sedes coworking."))
                .servers(List.of(devServer));
    }
}
package ar.edu.unju.fi.poo.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API REST - Sistema de Ventas y Cuotas (TP12)")
                        .version("1.0.0")
                        .description("Documentación interactiva de los endpoints de Clientes, Productos, Ventas y Cuotas - POO 2026 (UNJu San Pedro)")
                        .contact(new Contact()
                                .name("Grupo 6 - APU UNJu")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Servidor local de desarrollo")
                ));
    }
}
package com.fiap.gastrolinkapi;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(
        title = "Gastro Link API",
        version = "1.0.0",
        description = "API para o sistema Gastro Link, conectando clientes e donos de restaurantes",
        contact = @Contact(
            name = "Suporte Gastro Link",
            email = "contato@gastrolink.com"
        )
    )
)
public class GastroLinkApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(GastroLinkApiApplication.class, args);
    }

}

package ru.rogotovskiy.game_library_api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI api() {
        return new OpenAPI()
                .servers(
                        List.of(
                                new Server().url("http://localhost:8080")
                        )
                )
                .info(
                        new Info()
                                .title("Game library API")
                                .version("1.0.0")
                                .contact(
                                        new Contact()
                                                .name("Rogotovskiy Dmitriy")
                                                .email("drogotovsky@gmail.com")
                                )
                );
    }
}

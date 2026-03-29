package com.Gestao_de_Contas.security.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                // ─── Informações gerais da API ───────────────────────
                .info(new Info()
                        .title("Gestão de Contas API")
                        .description("API para gerenciamento de dívidas e contas")
                        .version("1.0.0")
                )
                // ─── Define que a API usa JWT Bearer ─────────────────
                .addSecurityItem(new SecurityRequirement()
                        .addList(SECURITY_SCHEME_NAME)
                )
                // ─── Configura o botão Authorize no Swagger UI ───────
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME,
                                new SecurityScheme()
                                        .name(SECURITY_SCHEME_NAME)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Cole seu token JWT aqui. Ex: eyJhbGci...")
                        )
                );
    }
}
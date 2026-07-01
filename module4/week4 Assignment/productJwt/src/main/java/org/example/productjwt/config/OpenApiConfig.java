package org.example.productjwt.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI productJwtOpenApi() {
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
            .info(new Info()
                .title("Product JWT API")
                .version("v1")
                .description("API documentation for Product, Customer, and Order management with JWT authentication.")
                .contact(new Contact().name("ProductJwt Team")))
            .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
            .schemaRequirement(securitySchemeName, new SecurityScheme()
                .name(securitySchemeName)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT"));
    }
}


package com.nathan.usermanagementapi.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * Configuration class for OpenAPI 3.0 (Swagger) documentation.
 *
 * This class creates and configures the OpenAPI bean that defines
 * the API documentation for the User Management API.
 */
@Configuration
public class OpenApiConfig {

    /**
     * Creates and returns a customized OpenAPI instance.
     * This method configures the API documentation with details like:
     * - API title and version
     * - Description
     * - Contact information
     * - License information
     * - Security schemes (JWT)
     * - Server configurations
     *
     * @return A configured OpenAPI instance
     */
    @Bean
    public OpenAPI customOpenAPI() {
        // Create contact information
        Contact contact = new Contact()
                .email("boutchouangelija@gmail.com")
                .name("Boutchouang Nathan Elija");

        // Create license information
        License mitLicense = new License()
                .name("MIT License")
                .url("https://choosealicense.com/licenses/mit/");

        // Create security scheme for JWT Bearer Authentication
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");

        // Create security requirement
        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList("bearerAuth");

        // Define server
        Server server = new Server()
                .url("http://localhost:8080")
                .description("Local Development Server");

        // Create and return a customized OpenAPI instance
        return new OpenAPI()
                .info(new Info()
                        .title("User Management API")
                        .version("1.0")
                        .contact(contact)
                        .description("API for managing users and authentication with JWT. To use protected endpoints, first call the login endpoint to get a token, then click the 'Authorize' button at the top and enter the token.")
                        .license(mitLicense))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", securityScheme))
                .addSecurityItem(securityRequirement)
                .servers(Arrays.asList(server));
    }
}
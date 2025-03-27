package com.carbonaro.ReactiveSimplifiedPicPay.core.configs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@OpenAPIDefinition
public class SwaggerConfig {

    private static final String DEFAULT_API_TITLE = "Simplified PicPay BackEnd - Reactive";
    private static final String DEFAULT_API_VERSION = "1.0.0";
    private static final String DEFAULT_API_DESCRIPTION = "A web service using reactive programming to solve PicPay challenge";

    @Bean
    public OpenAPI buildAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title(DEFAULT_API_TITLE)
                        .version(DEFAULT_API_VERSION)
                        .description(DEFAULT_API_DESCRIPTION))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))
                .paths(new Paths()
                        .addPathItem("/oauth/register", new PathItem().post(new Operation().security(List.of())))
                        .addPathItem("/oauth/login", new PathItem().post(new Operation().security(List.of()))));
    }

}
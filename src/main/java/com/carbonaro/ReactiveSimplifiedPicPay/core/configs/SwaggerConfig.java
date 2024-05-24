package com.carbonaro.ReactiveSimplifiedPicPay.core.configs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition
public class SwaggerConfig {

    private static final String DEFAULT_API_TITLE = "Simplified PicPay BackEnd - Reactive";
    private static final String DEFAULT_API_VERSION = "1.0.0";
    private static final String DEFAULT_API_DESCRIPTION = "A web service using reactive programming to solve PicPay challenge";

    @Bean
    public OpenAPI buildAPI() {

        return new OpenAPI()
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title(DEFAULT_API_TITLE)
                        .version(DEFAULT_API_VERSION)
                        .description(DEFAULT_API_DESCRIPTION)
                );
    }
}
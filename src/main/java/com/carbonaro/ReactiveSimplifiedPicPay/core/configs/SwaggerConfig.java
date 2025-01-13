package com.carbonaro.ReactiveSimplifiedPicPay.core.configs;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.HEADER;
import static java.lang.Boolean.TRUE;
import static java.util.Objects.nonNull;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.info.Info;

@Configuration
@OpenAPIDefinition
public class SwaggerConfig {

    private static final String DEFAULT_API_TITLE = "Simplified PicPay BackEnd - Reactive";
    private static final String DEFAULT_API_VERSION = "1.0.0";
    private static final String DEFAULT_API_DESCRIPTION = "A web service using reactive programming to solve PicPay challenge";

    private static final String API_TOKEN = "AUTH-API-TOKEN";
    private static final String USER_AUTHORIZATION = "Authorization (USER LEVEL NEEDED)";
    private static final String ADMIN_AUTHORIZATION = "Authorization (ADMIN LEVEL NEEDED)";

    @Bean
    public OpenAPI buildAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title(DEFAULT_API_TITLE)
                        .version(DEFAULT_API_VERSION)
                        .description(DEFAULT_API_DESCRIPTION)
                );
    }

    @Bean
    public OpenApiCustomizer customOpenApi() {

        return openApi -> openApi
                .getPaths()
                .forEach(this::getRequests);
    }

    private void getRequests(String path, PathItem pathItem) {

        if (nonNull(pathItem.getGet()) || nonNull(pathItem.getGet()) && path.startsWith("/wallet")) {
            pathItem.getGet().addParametersItem(createUserTokenParameter());
        }

        if (nonNull(pathItem.getPost()) && !path.toLowerCase().contains("/token") || nonNull(pathItem.getPost()) && path.startsWith("/wallet")) {
            pathItem.getPost().addParametersItem(createAdminTokenParameter());
        }

        if (nonNull(pathItem.getPut())) {
            pathItem.getPut().addParametersItem(createAdminTokenParameter());
        }

        if (nonNull(pathItem.getPatch())) {
            pathItem.getPatch().addParametersItem(createAdminTokenParameter());
        }

        if (nonNull(pathItem.getDelete())) {
            pathItem.getDelete().addParametersItem(createAdminTokenParameter());
        }

    }

    private Parameter createUserTokenParameter() {

        return new Parameter()
                .in(HEADER.toString())
                .schema(new StringSchema())
                .name(API_TOKEN)
                .description(USER_AUTHORIZATION)
                .required(TRUE);
    }

    private Parameter createAdminTokenParameter() {

        return new Parameter()
                .in(HEADER.toString())
                .schema(new StringSchema())
                .name(API_TOKEN)
                .description(ADMIN_AUTHORIZATION)
                .required(TRUE);
    }

}
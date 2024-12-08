package com.carbonaro.ReactiveSimplifiedPicPay.api.annotations.RouteParams;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Parameter(
        in = ParameterIn.QUERY,
        name = "employeesNumber",
        description = "Number of enterprise employees.",
        schema = @Schema(implementation = Integer.class),
        required = true)
@Parameter(
        in = ParameterIn.QUERY,
        name = "name",
        description = "Enterprise full name.",
        schema = @Schema(implementation = String.class),
        required = true)
@Parameter(
        in = ParameterIn.QUERY,
        name = "email",
        description = "Enterprise full email.",
        schema = @Schema(implementation = String.class,
                required = true))
@Parameter(
        in = ParameterIn.QUERY,
        name = "address",
        description = "Enterprise full address.",
        schema = @Schema(implementation = String.class),
        required = true)
@Parameter(
        in = ParameterIn.QUERY,
        name = "password",
        description = "Enterprise full and secret password.",
        schema = @Schema(implementation = String.class),
        required = true)
public @interface LegalPersonRequestAsQueryParam {
}

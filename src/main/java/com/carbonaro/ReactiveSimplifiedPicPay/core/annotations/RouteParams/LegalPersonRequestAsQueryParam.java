package com.carbonaro.ReactiveSimplifiedPicPay.core.annotations.RouteParams;

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
        schema = @Schema(type = "Integer"))
@Parameter(
        in = ParameterIn.QUERY,
        name = "name",
        description = "Enterprise full name.",
        schema = @Schema(type = "String"))
@Parameter(
        in = ParameterIn.QUERY,
        name = "email",
        description = "Enterprise full email.",
        schema = @Schema(type = "String"))
@Parameter(
        in = ParameterIn.QUERY,
        name = "address",
        description = "Enterprise full address.",
        schema = @Schema(type = "String"))
@Parameter(
        in = ParameterIn.QUERY,
        name = "password",
        description = "Enterprise full and secret password.",
        schema = @Schema(type = "String"))
public @interface LegalPersonRequestAsQueryParam {
}

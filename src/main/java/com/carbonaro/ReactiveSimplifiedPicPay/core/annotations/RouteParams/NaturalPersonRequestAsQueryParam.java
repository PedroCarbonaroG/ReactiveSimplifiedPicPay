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
        name = "birthDate",
        description = "Day of birth from natural person",
        schema = @Schema(description = "Format dd/MM/yyyy", type = "LocalDate"),
        required = true)
@Parameter(
        in = ParameterIn.QUERY,
        name = "name",
        description = "Natural person full name.",
        schema = @Schema(type = "String"),
        required = true)
@Parameter(
        in = ParameterIn.QUERY,
        name = "email",
        description = "Natural person full email.",
        schema = @Schema(type = "String"),
        required = true)
@Parameter(
        in = ParameterIn.QUERY,
        name = "address",
        description = "Natural person full address.",
        schema = @Schema(type = "String"),
        required = true)
@Parameter(
        in = ParameterIn.QUERY,
        name = "password",
        description = "Natural person full and secret password.",
        schema = @Schema(type = "String"),
        required = true)
public @interface NaturalPersonRequestAsQueryParam {
}

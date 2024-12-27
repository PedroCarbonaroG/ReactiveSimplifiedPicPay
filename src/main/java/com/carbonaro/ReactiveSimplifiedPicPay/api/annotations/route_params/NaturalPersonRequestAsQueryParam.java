package com.carbonaro.ReactiveSimplifiedPicPay.api.annotations.route_params;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.LocalDate;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Parameter(
        in = ParameterIn.QUERY,
        name = "birthDate",
        description = "Day of birth from natural person (dd/mm/yyyy)",
        schema = @Schema(implementation = LocalDate.class))
@Parameter(
        in = ParameterIn.QUERY,
        name = "name",
        description = "Natural person full name.",
        schema = @Schema(implementation = String.class))
@Parameter(
        in = ParameterIn.QUERY,
        name = "email",
        description = "Natural person full email.",
        schema = @Schema(implementation = String.class))
@Parameter(
        in = ParameterIn.QUERY,
        name = "address",
        description = "Natural person full address.",
        schema = @Schema(implementation = String.class))
@Parameter(
        in = ParameterIn.QUERY,
        name = "password",
        description = "Natural person full and secret password.",
        schema = @Schema(implementation = String.class))
public @interface NaturalPersonRequestAsQueryParam {
}

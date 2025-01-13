package com.carbonaro.ReactiveSimplifiedPicPay.api.annotations.route_params;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.math.BigDecimal;
import java.time.LocalDate;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Parameter(
        in = ParameterIn.QUERY,
        name = "senderDocument",
        description = "Adding sender document to filtering search",
        schema = @Schema(implementation = String.class))
@Parameter(
        in = ParameterIn.QUERY,
        name = "receiverDocument",
        description = "Adding receiver document to filtering search",
        schema = @Schema(implementation = String.class))
@Parameter(
        in = ParameterIn.QUERY,
        name = "initialDate",
        description = "Adding initial date to filtering search",
        schema = @Schema(implementation = LocalDate.class))
@Parameter(
        in = ParameterIn.QUERY,
        name = "finalDate",
        description = "Adding final date to filtering search",
        schema = @Schema(implementation = LocalDate.class))
@Parameter(
        in = ParameterIn.QUERY,
        name = "initialTransactionValue",
        description = "Adding initial transaction value to filtering search",
        schema = @Schema(implementation = BigDecimal.class))
@Parameter(
        in = ParameterIn.QUERY,
        name = "finalTransactionValue",
        description = "Adding final transaction value to filtering search",
        schema = @Schema(implementation = BigDecimal.class))
public @interface TransactionFilterRequestAsQueryParam {
}

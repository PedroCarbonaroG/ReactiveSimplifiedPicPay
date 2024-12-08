package com.carbonaro.ReactiveSimplifiedPicPay.api.annotations.RouteParams;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.math.BigDecimal;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Parameter(
        in = ParameterIn.QUERY,
        name = "senderDocument",
        description = "Sender Document",
        schema = @Schema(implementation = String.class),
        required = true)
@Parameter(
        in = ParameterIn.QUERY,
        name = "receiverDocument",
        description = "Receiver Document",
        schema = @Schema(implementation = String.class),
        required = true)
@Parameter(
        in = ParameterIn.QUERY,
        name = "transactionValue",
        description = "Transaction Value",
        schema = @Schema(implementation = BigDecimal.class),
        required = true)
public @interface TransactionRequestAsQueryParam {
}

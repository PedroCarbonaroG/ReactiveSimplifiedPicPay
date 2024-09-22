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
        name = "senderDocument",
        description = "Sender Document",
        schema = @Schema(type = "String"))
@Parameter(
        in = ParameterIn.QUERY,
        name = "receiverDocument",
        description = "Receiver Document",
        schema = @Schema(type = "String"))
@Parameter(
        in = ParameterIn.QUERY,
        name = "transactionValue",
        description = "Transaction Value",
        schema = @Schema(type = "BigDecimal"))
public @interface TransactionRequestAsQueryParam {
}

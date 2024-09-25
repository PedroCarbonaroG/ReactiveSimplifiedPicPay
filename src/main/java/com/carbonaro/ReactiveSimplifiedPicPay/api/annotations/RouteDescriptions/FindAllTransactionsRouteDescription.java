package com.carbonaro.ReactiveSimplifiedPicPay.api.annotations.RouteDescriptions;

import com.carbonaro.ReactiveSimplifiedPicPay.api.exception_handler.response.ErrorEmptyResponse;
import com.carbonaro.ReactiveSimplifiedPicPay.api.exception_handler.response.ErrorResponse;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.responses.transaction.TransactionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Operation(
        summary = "Responsible route for return all Transactions.",
        responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "OK",
                        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = TransactionResponse.class))),
                @ApiResponse(
                        responseCode = "204",
                        description = "Neither Transactions was found.",
                        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorEmptyResponse.class))),
                @ApiResponse(
                        responseCode = " 404 • 500",
                        description = "If something goes wrong if data or application resources, returns treated error.",
                        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),})
public @interface FindAllTransactionsRouteDescription {
}

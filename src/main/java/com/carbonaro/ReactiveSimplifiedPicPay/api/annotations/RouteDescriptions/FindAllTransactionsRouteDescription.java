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
                        responseCode = "404",
                        description = "Not Found. Resources are not found to complete the service provided by this endpoint..",
                        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),

                @ApiResponse(
                        responseCode = "500",
                        description = "Internal Server Error. Something went wrong with API, contact the administration.",
                        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))})
public @interface FindAllTransactionsRouteDescription {
}

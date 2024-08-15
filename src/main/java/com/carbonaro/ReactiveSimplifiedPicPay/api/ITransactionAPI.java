package com.carbonaro.ReactiveSimplifiedPicPay.api;

import com.carbonaro.ReactiveSimplifiedPicPay.core.exceptionHandler.response.ErrorEmptyResponse;
import com.carbonaro.ReactiveSimplifiedPicPay.core.exceptionHandler.response.ErrorResponse;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.requests.TransactionRequest;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.responses.transaction.TransactionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Tag(name = "Transaction API - The NaturalPerson and LegalPerson transactions handler.")
@RequestMapping(value = "/transactions", produces = MediaType.APPLICATION_JSON_VALUE)
public interface ITransactionAPI {

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
    @GetMapping
    Flux<TransactionResponse> findAllTransactions();

    @Operation(
            summary = "Responsible route for save a new Transaction.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK"),

                    @ApiResponse(
                            responseCode = "400",
                            description = "Some(s) parameter(s) could not match, revise that and try again with right parameters!",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),

                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found. Resources are not found to complete the service provided by this endpoint.",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),

                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error. Something went wrong with API, contact the administration.",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))})
    @PostMapping
    Mono<Void> saveTransaction(@RequestBody TransactionRequest transaction);

}

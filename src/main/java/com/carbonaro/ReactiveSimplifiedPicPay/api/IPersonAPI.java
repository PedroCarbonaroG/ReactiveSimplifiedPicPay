package com.carbonaro.ReactiveSimplifiedPicPay.api;

import com.carbonaro.ReactiveSimplifiedPicPay.core.exceptionHandler.response.ErrorEmptyResponse;
import com.carbonaro.ReactiveSimplifiedPicPay.core.exceptionHandler.response.ErrorResponse;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.responses.LegalPersonResponse;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.responses.NaturalPersonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Flux;

@Tag(name = "Person API - Natural and Legal Do's")
@RequestMapping(value = "/person", produces = MediaType.APPLICATION_JSON_VALUE)
public interface IPersonAPI {

    @Operation(
            summary = "Responsible route to return all LegalPersons in DataBase.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = LegalPersonResponse.class))),

                    @ApiResponse(
                            responseCode = "204",
                            description = "Neither LegalPerson was found.",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorEmptyResponse.class))),

                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found. Resources are not found to complete the search for all LegalPersons.",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),

                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error. Something went wrong with API, contact the administration.",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
            })
    @GetMapping(value = "/legals")
    Flux<LegalPersonResponse> findAllLegals();

    @Operation(
            summary = "Responsible route to return all NaturalPersons in DataBase.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = NaturalPersonResponse.class))),

                    @ApiResponse(
                            responseCode = "204",
                            description = "Neither NaturalPerson was found.",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorEmptyResponse.class))),

                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found. Resources are not found to complete the search for all NaturalPersons.",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),

                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error. Something went wrong with API, contact the administration.",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),})
    @GetMapping(value = "/naturals")
    Flux<NaturalPersonResponse> findAllNaturals();
}

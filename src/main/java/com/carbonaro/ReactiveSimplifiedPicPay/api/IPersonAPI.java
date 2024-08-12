package com.carbonaro.ReactiveSimplifiedPicPay.api;

import com.carbonaro.ReactiveSimplifiedPicPay.core.exceptionHandler.response.ErrorEmptyResponse;
import com.carbonaro.ReactiveSimplifiedPicPay.core.exceptionHandler.response.ErrorResponse;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.requests.LegalPersonRequest;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.requests.NaturalPersonRequest;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.responses.person.LegalPersonResponse;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.responses.person.NaturalPersonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Tag(name = "Person API - Natural and Legal Do's")
@RequestMapping(value = "/person", produces = MediaType.APPLICATION_JSON_VALUE)
public interface IPersonAPI {

    @Operation(
            summary = "Responsible route to return all LegalPersons.",
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
                            description = "Not Found. Resources are not found to complete the service provided by this endpoint.",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),

                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error. Something went wrong with API, contact the administration.",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping(value = "/legals")
    Flux<LegalPersonResponse> findAllLegals();

    @Operation(
            summary = "Responsible route for return LegalPerson by your own CNPJ.",
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
    @GetMapping(value = "/legals/{cnpj}")
    Mono<LegalPersonResponse> findLegalByCNPJ(@PathVariable String cnpj);

    @Operation(
            summary = "Responsible route for return LegalPerson by your own Identifier(ID).",
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
    @GetMapping(value = "/legals/id/{id}")
    Mono<LegalPersonResponse> findLegalById(@PathVariable String id);

    @Operation(
            summary = "Responsible route for save a new LegalPerson.",
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
    @PostMapping("/legal/save")
    Mono<Void> saveLegalPerson(@RequestBody LegalPersonRequest legalPerson);

    @Operation
    @PostMapping("/legal/partner")
    Mono<Void> savePartnerToLegalPerson(@RequestParam String cnpj, @RequestBody NaturalPersonRequest naturalPerson);

    @Operation
    @PatchMapping("/legal")
    Mono<Void> updateLegalPerson(@RequestParam String cnpj, @RequestBody LegalPersonRequest legalPerson);

    @Operation
    @DeleteMapping("/legal")
    Mono<Void> deleteLegalPerson(@RequestBody LegalPersonRequest legalPerson);

    @Operation
    @DeleteMapping("/legal/partner")
    Mono<Void> deletePartnerByLegalPerson(@RequestParam String cnpj, @RequestBody NaturalPersonRequest naturalPersonRequest);

    @Operation(
            summary = "Responsible route to return all NaturalPersons.",
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
                            description = "Not Found. Resources are not found to complete the service provided by this endpoint.",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),

                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error. Something went wrong with API, contact the administration.",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),})
    @GetMapping(value = "/naturals")
    Flux<NaturalPersonResponse> findAllNaturals();

    @Operation(
            summary = "Responsible route for return NaturalPerson by your own CPF.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = LegalPersonResponse.class))),

                    @ApiResponse(
                            responseCode = "204",
                            description = "Neither NaturalPerson was found.",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorEmptyResponse.class))),

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
    @GetMapping(value = "/naturals/{cpf}")
    Mono<NaturalPersonResponse> findNaturalByCPF(@PathVariable String cpf);

    @Operation(
            summary = "Responsible route for return NaturalPerson by your own Identifier(ID).",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = LegalPersonResponse.class))),

                    @ApiResponse(
                            responseCode = "204",
                            description = "Neither NaturalPerson was found.",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorEmptyResponse.class))),

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
    @GetMapping(value = "/naturals/id/{id}")
    Mono<NaturalPersonResponse> findNaturalByID(@PathVariable String id);

    @Operation
    @PostMapping("/natural")
    Mono<Void> saveNaturalPerson(@RequestBody NaturalPersonRequest naturalPerson);

    @Operation(
            summary = "Responsible route to update an NaturalPerson.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Neither LegalPerson was found.",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorEmptyResponse.class))),

                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found. Resources are not found to complete the service provided by this endpoint.",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),

                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error. Something went wrong with API, contact the administration.",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))})
    @PutMapping("/natural")
    Mono<Void> updateNaturalPerson(@RequestBody NaturalPersonRequest naturalPerson, @Parameter String cpf);

    @Operation
    @DeleteMapping("/natural")
    Mono<Void> deleteNaturalPerson(@RequestBody NaturalPersonRequest naturalPerson);

}
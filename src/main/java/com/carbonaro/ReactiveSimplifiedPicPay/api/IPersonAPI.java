package com.carbonaro.ReactiveSimplifiedPicPay.api;

import com.carbonaro.ReactiveSimplifiedPicPay.core.annotations.RouteDescriptions.*;
import com.carbonaro.ReactiveSimplifiedPicPay.core.annotations.RouteParams.LegalPersonRequestAsQueryParam;
import com.carbonaro.ReactiveSimplifiedPicPay.core.exception_handler.response.ErrorEmptyResponse;
import com.carbonaro.ReactiveSimplifiedPicPay.core.exception_handler.response.ErrorResponse;
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

    @GetMapping("/list-all/legals")
    @FindAllLegalsRouteDescription
    Flux<LegalPersonResponse> findAllLegals();

    @GetMapping("/legals/{cnpj}")
    @FindLegalByCNPJRouteDescription
    Mono<LegalPersonResponse> findLegalByCNPJ(@PathVariable String companyCNPJ);

    @GetMapping("/legals/id")
    @FindLegalByIdRouteDescription
    Mono<LegalPersonResponse> findLegalById(@Parameter String id);

    @PostMapping("/legal/save")
    @LegalPersonRequestAsQueryParam
    @SaveLegalPersonRouteDescription
    Mono<Void> saveLegalPerson(@Parameter(hidden = true) LegalPersonRequest legalPerson);

    @PostMapping("/legal/save/partner")
    @SavePartnerToLegalPersonRouteDescription
    Mono<Void> savePartnerToLegalPerson(@RequestParam String companyCNPJ, @RequestParam String partnerCPF);

    @PatchMapping("/legal")
    @LegalPersonRequestAsQueryParam
    @UpdateLegalPersonRouteDescription
    Mono<Void> updateLegalPerson(@RequestParam String companyCNPJ, @Parameter(hidden = true) LegalPersonRequest legalPerson);

    @DeleteMapping("/legal")
    @DeleteLegalPersonRouteDescription
    Mono<Void> deleteLegalPerson(@RequestParam String legalPersonCNPJ);

    @DeleteMapping("/legal/partner")
    @DeletePartnerByLegalPersonRouteDescription
    Mono<Void> deletePartnerByLegalPerson(@RequestParam String companyCNPJ, @RequestParam String partnerCPF);

    @GetMapping("/list-all/naturals")
    @FindAllNaturalsRouteDescription
    Flux<NaturalPersonResponse> findAllNaturals();

    @GetMapping("/naturals/{cpf}")
    @FindNaturalByCPFRouteDescription
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
    @GetMapping(value = "/naturals/id")
    Mono<NaturalPersonResponse> findNaturalByID(@Parameter String id);

    @Operation
    @PostMapping("/natural/save")
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
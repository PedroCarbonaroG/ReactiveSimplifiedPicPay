package com.carbonaro.ReactiveSimplifiedPicPay.api;

import com.carbonaro.ReactiveSimplifiedPicPay.api.annotations.RouteDescriptions.*;
import com.carbonaro.ReactiveSimplifiedPicPay.api.annotations.RouteParams.LegalPersonRequestAsQueryParam;
import com.carbonaro.ReactiveSimplifiedPicPay.api.annotations.RouteParams.NaturalPersonRequestAsQueryParam;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.requests.person.LegalPersonRequest;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.requests.person.NaturalPersonRequest;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.responses.person.LegalPersonResponse;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.responses.person.NaturalPersonResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Tag(name = "Person API - Person management")
@RequestMapping(value = "/person", produces = MediaType.APPLICATION_JSON_VALUE)
public interface IPersonAPI {

    @GetMapping("/list-all/legals")
    @FindAllLegalsRouteDescription
    Flux<LegalPersonResponse> findAllLegals();

    @GetMapping("/legal/companyCNPJ")
    @FindLegalByCNPJRouteDescription
    Mono<LegalPersonResponse> findLegalByCNPJ(@Parameter String companyCNPJ);

    @GetMapping("/legal/id")
    @FindLegalByIdRouteDescription
    Mono<LegalPersonResponse> findLegalById(@Parameter String id);

    @PostMapping("/legal/save")
    @LegalPersonRequestAsQueryParam
    @SaveLegalPersonRouteDescription
    Mono<Void> saveLegalPerson(@Parameter(hidden = true) LegalPersonRequest legalPerson);

    @PostMapping("/legal/save/partner")
    @SavePartnerToLegalPersonRouteDescription
    Mono<Void> savePartnerToLegalPerson(@RequestParam String companyCNPJ, @RequestParam String partnerCPF);

    @PatchMapping("/legal/update")
    @LegalPersonRequestAsQueryParam
    @UpdateLegalPersonRouteDescription
    Mono<Void> updateLegalPerson(@RequestParam String companyCNPJ, @Parameter(hidden = true) LegalPersonRequest legalPerson);

    @DeleteMapping("/legal/delete")
    @DeleteLegalPersonRouteDescription
    Mono<Void> deleteLegalPerson(@RequestParam String companyCNPJ);

    @DeleteMapping("/legal/partner/delete")
    @DeletePartnerByLegalPersonRouteDescription
    Mono<Void> deletePartnerByLegalPerson(@RequestParam String partnerCPF);

    @GetMapping("/list-all/naturals")
    @FindAllNaturalsRouteDescription
    Flux<NaturalPersonResponse> findAllNaturals();

    @GetMapping("/natural/{cpf}")
    @FindNaturalByCPFRouteDescription
    Mono<NaturalPersonResponse> findNaturalByCPF(@PathVariable String cpf);

    @GetMapping("/natural/id")
    @FindNaturalByIDRouteDescription
    Mono<NaturalPersonResponse> findNaturalByID(@Parameter String id);

    @PostMapping("/natural/save")
    @NaturalPersonRequestAsQueryParam
    @SaveNaturalPersonRouteDescription
    Mono<Void> saveNaturalPerson(@Parameter(hidden = true) NaturalPersonRequest naturalPerson,
                                 @Parameter(required = true) String cpf);

    @PatchMapping("/natural/update")
    @NaturalPersonRequestAsQueryParam
    @UpdateNaturalPersonRouteDescription
    Mono<Void> updateNaturalPerson(@Parameter String cpf, @Parameter(hidden = true) NaturalPersonRequest naturalPerson);

    @DeleteMapping("/natural/delete")
    @DeleteNaturalPersonRouteDescription
    Mono<Void> deleteNaturalPerson(@Parameter String cpf);

}
package com.carbonaro.ReactiveSimplifiedPicPay.api;

import com.carbonaro.ReactiveSimplifiedPicPay.core.annotations.RouteDescriptions.*;
import com.carbonaro.ReactiveSimplifiedPicPay.core.annotations.RouteParams.LegalPersonRequestAsQueryParam;
import com.carbonaro.ReactiveSimplifiedPicPay.core.annotations.RouteParams.NaturalPersonRequestAsQueryParam;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.requests.LegalPersonRequest;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.requests.NaturalPersonRequest;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.responses.person.LegalPersonResponse;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.responses.person.NaturalPersonResponse;
import io.swagger.v3.oas.annotations.Parameter;
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

    @GetMapping("/legal/{cnpj}")
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
    Mono<Void> deletePartnerByLegalPerson(@RequestParam String companyCNPJ, @RequestParam String partnerCPF);

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
    Mono<Void> saveNaturalPerson(@Parameter(hidden = true) NaturalPersonRequest naturalPerson);

    @PutMapping("/natural/update")
    @NaturalPersonRequestAsQueryParam
    @UpdateNaturalPersonRouteDescription
    Mono<Void> updateNaturalPerson(@Parameter String cpf, @Parameter(hidden = true) NaturalPersonRequest naturalPerson);

    @DeleteMapping("/natural/delete")
    @DeleteNaturalPersonRouteDescription
    Mono<Void> deleteNaturalPerson(@Parameter String cpf);

}
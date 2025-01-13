package com.carbonaro.ReactiveSimplifiedPicPay.api;

import com.carbonaro.ReactiveSimplifiedPicPay.api.annotations.route_description.DeleteLegalPersonRouteDescription;
import com.carbonaro.ReactiveSimplifiedPicPay.api.annotations.route_description.DeleteNaturalPersonRouteDescription;
import com.carbonaro.ReactiveSimplifiedPicPay.api.annotations.route_description.DeletePartnerByLegalPersonRouteDescription;
import com.carbonaro.ReactiveSimplifiedPicPay.api.annotations.route_description.FindAllLegalsRouteDescription;
import com.carbonaro.ReactiveSimplifiedPicPay.api.annotations.route_description.FindAllNaturalsRouteDescription;
import com.carbonaro.ReactiveSimplifiedPicPay.api.annotations.route_description.FindLegalByCNPJRouteDescription;
import com.carbonaro.ReactiveSimplifiedPicPay.api.annotations.route_description.FindLegalByIdRouteDescription;
import com.carbonaro.ReactiveSimplifiedPicPay.api.annotations.route_description.FindNaturalByCPFRouteDescription;
import com.carbonaro.ReactiveSimplifiedPicPay.api.annotations.route_description.FindNaturalByIDRouteDescription;
import com.carbonaro.ReactiveSimplifiedPicPay.api.annotations.route_description.LegalPersonFilterRequestAsQueryParam;
import com.carbonaro.ReactiveSimplifiedPicPay.api.annotations.route_description.SaveLegalPersonRouteDescription;
import com.carbonaro.ReactiveSimplifiedPicPay.api.annotations.route_description.SaveNaturalPersonRouteDescription;
import com.carbonaro.ReactiveSimplifiedPicPay.api.annotations.route_description.SavePartnerToLegalPersonRouteDescription;
import com.carbonaro.ReactiveSimplifiedPicPay.api.annotations.route_description.UpdateLegalPersonRouteDescription;
import com.carbonaro.ReactiveSimplifiedPicPay.api.annotations.route_description.UpdateNaturalPersonRouteDescription;
import com.carbonaro.ReactiveSimplifiedPicPay.api.annotations.route_params.LegalPersonRequestAsQueryParam;
import com.carbonaro.ReactiveSimplifiedPicPay.api.annotations.route_params.NaturalPersonFilterRequestAsQueryParam;
import com.carbonaro.ReactiveSimplifiedPicPay.api.annotations.route_params.NaturalPersonRequestAsQueryParam;
import com.carbonaro.ReactiveSimplifiedPicPay.api.requests.person.LegalPersonFilterRequest;
import com.carbonaro.ReactiveSimplifiedPicPay.api.requests.person.LegalPersonRequest;
import com.carbonaro.ReactiveSimplifiedPicPay.api.requests.person.NaturalPersonFilterRequest;
import com.carbonaro.ReactiveSimplifiedPicPay.api.requests.person.NaturalPersonRequest;
import com.carbonaro.ReactiveSimplifiedPicPay.api.responses.PageResponse;
import com.carbonaro.ReactiveSimplifiedPicPay.api.responses.person.LegalPersonResponse;
import com.carbonaro.ReactiveSimplifiedPicPay.api.responses.person.NaturalPersonResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@Tag(name = "Person API - Natural and Legal environment")
@RequestMapping(value = "/person", produces = MediaType.APPLICATION_JSON_VALUE)
public interface IPersonAPI {

    @GetMapping("/list-all/legals")
    @PageableAsQueryParam
    @FindAllLegalsRouteDescription
    @LegalPersonFilterRequestAsQueryParam
    Mono<PageResponse<LegalPersonResponse>> findAllLegals(@Parameter(hidden = true) LegalPersonFilterRequest filterRequest);

    @GetMapping("/legal/cnpj")
    @FindLegalByCNPJRouteDescription
    Mono<LegalPersonResponse> findLegalByCNPJ(@RequestHeader String companyCNPJ);

    @GetMapping("/legal/id")
    @FindLegalByIdRouteDescription
    Mono<LegalPersonResponse> findLegalById(@Parameter String id);

    @PostMapping("/legal/save")
    @LegalPersonRequestAsQueryParam
    @SaveLegalPersonRouteDescription
    Mono<Void> saveLegalPerson(@Parameter(hidden = true) LegalPersonRequest legalPerson);

    @PostMapping("/legal/save/partner")
    @SavePartnerToLegalPersonRouteDescription
    Mono<Void> savePartnerToLegalPerson(@RequestHeader String companyCNPJ, @RequestHeader String partnerCPF);

    @PatchMapping("/legal/update")
    @LegalPersonRequestAsQueryParam
    @UpdateLegalPersonRouteDescription
    Mono<Void> updateLegalPerson(@RequestHeader String companyCNPJ, @Parameter(hidden = true) LegalPersonRequest legalPerson);

    @DeleteMapping("/legal/delete")
    @DeleteLegalPersonRouteDescription
    Mono<Void> deleteLegalPerson(@RequestHeader String companyCNPJ);

    @DeleteMapping("/legal/partner/delete")
    @DeletePartnerByLegalPersonRouteDescription
    Mono<Void> deletePartnerByLegalPerson(@RequestHeader String companyCNPJ, @RequestHeader String partnerCPF);

    @GetMapping("/list-all/naturals")
    @PageableAsQueryParam
    @FindAllNaturalsRouteDescription
    @NaturalPersonFilterRequestAsQueryParam
    Mono<PageResponse<NaturalPersonResponse>> findAllNaturals(@Parameter(hidden = true) NaturalPersonFilterRequest filterRequest);

    @GetMapping("/natural/cpf")
    @FindNaturalByCPFRouteDescription
    Mono<NaturalPersonResponse> findNaturalByCPF(@RequestHeader String cpf);

    @GetMapping("/natural/id")
    @FindNaturalByIDRouteDescription
    Mono<NaturalPersonResponse> findNaturalByID(@Parameter String id);

    @PostMapping("/natural/save")
    @NaturalPersonRequestAsQueryParam
    @SaveNaturalPersonRouteDescription
    Mono<Void> saveNaturalPerson(@Parameter(hidden = true) NaturalPersonRequest naturalPerson);

    @PatchMapping("/natural/update")
    @NaturalPersonRequestAsQueryParam
    @UpdateNaturalPersonRouteDescription
    Mono<Void> updateNaturalPerson(@RequestHeader String cpf, @Parameter(hidden = true) NaturalPersonRequest naturalPerson);

    @DeleteMapping("/natural/delete")
    @DeleteNaturalPersonRouteDescription
    Mono<Void> deleteNaturalPerson(@RequestHeader String cpf);

}
package com.carbonaro.ReactiveSimplifiedPicPay.api.impl;

import com.carbonaro.ReactiveSimplifiedPicPay.api.IPersonAPI;
import com.carbonaro.ReactiveSimplifiedPicPay.api.requests.person.LegalPersonFilterRequest;
import com.carbonaro.ReactiveSimplifiedPicPay.api.requests.person.LegalPersonRequest;
import com.carbonaro.ReactiveSimplifiedPicPay.api.requests.person.NaturalPersonFilterRequest;
import com.carbonaro.ReactiveSimplifiedPicPay.api.requests.person.NaturalPersonRequest;
import com.carbonaro.ReactiveSimplifiedPicPay.api.responses.PageResponse;
import com.carbonaro.ReactiveSimplifiedPicPay.api.responses.person.LegalPersonResponse;
import com.carbonaro.ReactiveSimplifiedPicPay.api.responses.person.NaturalPersonResponse;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.mappers.IPersonMapper;
import com.carbonaro.ReactiveSimplifiedPicPay.services.LegalPersonService;
import com.carbonaro.ReactiveSimplifiedPicPay.services.NaturalPersonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import static com.carbonaro.ReactiveSimplifiedPicPay.AppConstants.LIST_ALL_LEGALS_SCOPES;
import static com.carbonaro.ReactiveSimplifiedPicPay.AppConstants.LIST_ALL_NATURALS_SCOPES;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PersonImpl implements IPersonAPI {

    private final NaturalPersonService naturalPersonService;
    private final LegalPersonService legalPersonService;

    @Override
    @PreAuthorize(LIST_ALL_LEGALS_SCOPES)
    public Mono<PageResponse<LegalPersonResponse>> findAllLegals(LegalPersonFilterRequest filterRequest) {

        var page = PageRequest.of(filterRequest.getPage(), filterRequest.getSize());
        return legalPersonService
                .findAllLegals(page, filterRequest)
                .map(IPersonMapper.INSTANCE::toPageResponseLegalPersonResponse)
                .doOnSuccess(unused -> log.warn("Legals list was deployed with success!"));
    }

    @Override
    public Mono<LegalPersonResponse> findLegalByCNPJ(String companyCNPJ) {

        return legalPersonService
                .findLegalByCNPJ(companyCNPJ)
                .map(IPersonMapper.INSTANCE::toLegalPersonResponse)
                .doOnSuccess(unused -> log.warn("Legal person with CNPJ: {}, was found with success!", companyCNPJ));
    }

    @Override
    public Mono<LegalPersonResponse> findLegalById(String id) {

        return legalPersonService
                .findLegalById(id)
                .map(IPersonMapper.INSTANCE::toLegalPersonResponse)
                .doOnSuccess(unused -> log.warn("Legal person with ID: {}, was found with success!", id));
    }

    @Override
    public Mono<Void> saveLegalPerson(LegalPersonRequest legalPerson) {

        return Mono
                .just(legalPerson)
                .map(IPersonMapper.INSTANCE::toLegalPersonByRequest)
                .flatMap(legalPersonService::saveLegalPerson)
                .doOnSuccess(unused -> log.warn("Legal person with CNPJ: {}, was saved with success!", legalPerson.getCnpj()));
    }

    @Override
    public Mono<Void> savePartnerToLegalPerson(String cnpj, String partnerCPF) {

        return legalPersonService
                .savePartner(cnpj, partnerCPF)
                .doOnSuccess(unused -> log.warn("Partner with CPF: {}, was saved successfully to company with CNPJ: {}", partnerCPF, cnpj));
    }

    @Override
    public Mono<Void> updateLegalPerson(String cnpj, LegalPersonRequest legalPerson) {

        return Mono
                .just(legalPerson)
                .map(IPersonMapper.INSTANCE::toLegalPersonByRequest)
                .flatMap(response -> legalPersonService.updateLegalPerson(cnpj, response))
                .doOnSuccess(unused -> log.warn("Legal person with CNPJ: {}, was updated with success!", cnpj))
                .then();
    }

    @Override
    public Mono<Void> deleteLegalPerson(String companyCNPJ) {

        return Mono
                .just(companyCNPJ)
                .flatMap(legalPersonService::deleteLegalPerson)
                .doOnSuccess(unused -> log.warn("Legal person with CNPJ: {}, was deleted with success!", companyCNPJ));
    }

    @Override
    public Mono<Void> deletePartnerByLegalPerson(String companyCNPJ, String partnerCPF) {

        return legalPersonService
                .deletePartner(companyCNPJ, partnerCPF)
                .doOnSuccess(unused -> log.warn("Partner with CPF: {}, was deleted successfully from company with CNPJ: {}", partnerCPF, companyCNPJ));
    }

    @Override
    @PreAuthorize(LIST_ALL_NATURALS_SCOPES)
    public Mono<PageResponse<NaturalPersonResponse>> findAllNaturals(NaturalPersonFilterRequest filterRequest) {

        var page = PageRequest.of(filterRequest.getPage(), filterRequest.getSize());
        return naturalPersonService
                .findAllNaturals(page, filterRequest)
                .map(IPersonMapper.INSTANCE::toPageResponseNaturalPersonResponse)
                .doOnSuccess(unused -> log.warn("Naturals list was deployed with success!"));
    }

    @Override
    public Mono<NaturalPersonResponse> findNaturalByCPF(String cpf) {

        return naturalPersonService
                .findNaturalByCPF(cpf)
                .map(IPersonMapper.INSTANCE::toNaturalPersonResponse)
                .doOnSuccess(unused -> log.warn("Natural person with CPF: {}, was found with success!", cpf));
    }

    @Override
    public Mono<NaturalPersonResponse> findNaturalByID(String id) {

        return naturalPersonService
                .findNaturalById(id)
                .map(IPersonMapper.INSTANCE::toNaturalPersonResponse)
                .doOnSuccess(unused -> log.warn("Natural person with ID: {}, was found with success!", id));
    }

    @Override
    public Mono<Void> saveNaturalPerson(NaturalPersonRequest naturalPerson) {

        return Mono
                .just(naturalPerson)
                .map(IPersonMapper.INSTANCE::toNaturalPersonByRequest)
                .flatMap(naturalPersonService::saveNaturalPerson)
                .doOnSuccess(unused -> log.warn("Natural person with CPF: {}, was saved with success!", naturalPerson.getCpf()));
    }

    @Override
    public Mono<Void> updateNaturalPerson(String cpf, NaturalPersonRequest naturalPerson) {

        return Mono
                .just(naturalPerson)
                .map(IPersonMapper.INSTANCE::toNaturalPersonByRequest)
                .flatMap(self -> naturalPersonService.updateNaturalPerson(self, cpf))
                .doOnSuccess(unused -> log.warn("Natural person with CPF: {}, was updated with success!", cpf));
    }

    @Override
    public Mono<Void> deleteNaturalPerson(String cpf) {

        return Mono
                .just(cpf)
                .flatMap(naturalPersonService::deleteNatural)
                .doOnSuccess(unused -> log.warn("Natural person with CPF: {}, was deleted with success!", cpf));
    }

}
package com.carbonaro.ReactiveSimplifiedPicPay.api.impl;

import com.carbonaro.ReactiveSimplifiedPicPay.api.IPersonAPI;
import com.carbonaro.ReactiveSimplifiedPicPay.api.requests.person.LegalPersonFilterRequest;
import com.carbonaro.ReactiveSimplifiedPicPay.api.responses.PageResponse;
import com.carbonaro.ReactiveSimplifiedPicPay.core.security.SecurityScopes;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.mappers.IPersonMapper;
import com.carbonaro.ReactiveSimplifiedPicPay.api.requests.person.LegalPersonRequest;
import com.carbonaro.ReactiveSimplifiedPicPay.api.requests.person.NaturalPersonRequest;
import com.carbonaro.ReactiveSimplifiedPicPay.api.responses.person.LegalPersonResponse;
import com.carbonaro.ReactiveSimplifiedPicPay.api.responses.person.NaturalPersonResponse;
import com.carbonaro.ReactiveSimplifiedPicPay.services.LegalPersonService;
import com.carbonaro.ReactiveSimplifiedPicPay.services.NaturalPersonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.carbonaro.ReactiveSimplifiedPicPay.AppConstants.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class IPersonImpl implements IPersonAPI {

    private final NaturalPersonService naturalPersonService;
    private final LegalPersonService legalPersonService;

    @Override
    @SecurityScopes(scopes = {ADMIN_READ_SCOPE})
    public Mono<PageResponse<LegalPersonResponse>> findAllLegals(LegalPersonFilterRequest filterRequest) {

        var pageRequest = PageRequest.of(filterRequest.getPage(), filterRequest.getSize());
        return legalPersonService
                .listAll(pageRequest, filterRequest)
                .map(IPersonMapper.INSTANCE::toPageResponseLegalPersonResponse);
    }

    @Override
    @SecurityScopes(scopes = {ADMIN_READ_SCOPE})
    public Mono<LegalPersonResponse> findLegalByCNPJ(String companyCNPJ) {

        return legalPersonService
                .findLegalByCNPJ(companyCNPJ)
                .map(IPersonMapper.INSTANCE::toLegalPersonResponse);
    }

    @Override
    @SecurityScopes(scopes = {ADMIN_READ_SCOPE})
    public Mono<LegalPersonResponse> findLegalById(String id) {

        return legalPersonService
                .findLegalById(id)
                .map(IPersonMapper.INSTANCE::toLegalPersonResponse);
    }

    @Override //TODO ERRO AQUI
    @SecurityScopes(scopes = {ADMIN_WRITE_SCOPE})
    public Mono<Void> saveLegalPerson(LegalPersonRequest legalPerson) {

        return Mono
                .just(legalPerson)
                .map(IPersonMapper.INSTANCE::toLegalPersonByRequest)
                .flatMap(legalPersonService::saveLegalPerson);
    }

    @Override
    @SecurityScopes(scopes = {ADMIN_WRITE_SCOPE})
    public Mono<Void> savePartnerToLegalPerson(String cnpj, String partnerCPF) {

        return legalPersonService
                .savePartner(cnpj, partnerCPF);
    }

    @Override
    @SecurityScopes(scopes = {ADMIN_WRITE_SCOPE})
    public Mono<Void> updateLegalPerson(String cnpj, LegalPersonRequest legalPerson) {

        return Mono
                .just(legalPerson)
                .map(IPersonMapper.INSTANCE::toLegalPersonByRequest)
                .flatMap(response -> legalPersonService.updateLegalPerson(cnpj, response))
                .doOnSuccess(unused -> log.info("Company with CNPJ: {}, was updated successfully.", cnpj));
    }

    @Override
    @SecurityScopes(scopes = {ADMIN_WRITE_SCOPE})
    public Mono<Void> deleteLegalPerson(String companyCNPJ) {

        return legalPersonService
                .deleteLegal(companyCNPJ);
    }

    @Override
    @SecurityScopes(scopes = {ADMIN_WRITE_SCOPE})
    public Mono<Void> deletePartnerByLegalPerson(String partnerCPF) {

        return legalPersonService
                .deletePartner(partnerCPF);
    }

    @Override
    @SecurityScopes(scopes = {ADMIN_READ_SCOPE})
    public Flux<NaturalPersonResponse> findAllNaturals() {

        return naturalPersonService
                .findAllNaturals()
                .map(IPersonMapper.INSTANCE::toNaturalPersonResponse);
    }

    @Override
    @SecurityScopes(scopes = {ADMIN_READ_SCOPE})
    public Mono<NaturalPersonResponse> findNaturalByCPF(String cpf) {

        return naturalPersonService
                .findNaturalByCPF(cpf)
                .map(IPersonMapper.INSTANCE::toNaturalPersonResponse);
    }

    @Override
    @SecurityScopes(scopes = {ADMIN_READ_SCOPE})
    public Mono<NaturalPersonResponse> findNaturalByID(String id) {

        return naturalPersonService
                .findNaturalById(id)
                .map(IPersonMapper.INSTANCE::toNaturalPersonResponse);
    }

    @Override
    @SecurityScopes(scopes = {ADMIN_WRITE_SCOPE})
    public Mono<Void> saveNaturalPerson(NaturalPersonRequest naturalPerson, String cpf) {

        return Mono
                .just(naturalPerson)
                .map(IPersonMapper.INSTANCE::toNaturalPersonByRequest)
                .flatMap(natural -> naturalPersonService.saveNatural(natural, cpf));
    }

    @Override
    @SecurityScopes(scopes = {ADMIN_WRITE_SCOPE})
    public Mono<Void> updateNaturalPerson(String cpf, NaturalPersonRequest naturalPersonRequest) {

        return Mono
                .just(naturalPersonRequest)
                .map(IPersonMapper.INSTANCE::toNaturalPersonByRequest)
                .flatMap(naturalPerson -> naturalPersonService.updateNatural(naturalPerson, cpf));
    }

    @Override
    @SecurityScopes(scopes = {ADMIN_WRITE_SCOPE})
    public Mono<Void> deleteNaturalPerson(String cpf) {

        return naturalPersonService
                .deleteNatural(cpf);
    }

}
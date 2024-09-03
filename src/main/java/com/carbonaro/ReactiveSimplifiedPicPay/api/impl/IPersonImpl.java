package com.carbonaro.ReactiveSimplifiedPicPay.api.impl;

import com.carbonaro.ReactiveSimplifiedPicPay.api.IPersonAPI;
import com.carbonaro.ReactiveSimplifiedPicPay.core.security.SecuredDelegate;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.mappers.IPersonMapper;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.requests.LegalPersonRequest;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.requests.NaturalPersonRequest;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.responses.person.LegalPersonResponse;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.responses.person.NaturalPersonResponse;
import com.carbonaro.ReactiveSimplifiedPicPay.services.LegalPersonService;
import com.carbonaro.ReactiveSimplifiedPicPay.services.NaturalPersonService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.carbonaro.ReactiveSimplifiedPicPay.AppConstants.*;

@RestController
@AllArgsConstructor
public class IPersonImpl implements IPersonAPI {

    private final NaturalPersonService naturalPersonService;
    private final LegalPersonService legalPersonService;

    @Override
    @SecuredDelegate(scopes = {USER_READ_SCOPE})
    public Flux<LegalPersonResponse> findAllLegals() {

        return legalPersonService
                .findAllLegals()
                .map(IPersonMapper.INSTANCE::toLegalPersonResponse);
    }

    @Override
    @SecuredDelegate(scopes = {USER_READ_SCOPE})
    public Mono<LegalPersonResponse> findLegalByCNPJ(String companyCNPJ) {

        return legalPersonService
                .findLegalByCNPJ(companyCNPJ)
                .map(IPersonMapper.INSTANCE::toLegalPersonResponse);
    }

    @Override
    @SecuredDelegate(scopes = {USER_READ_SCOPE})
    public Mono<LegalPersonResponse> findLegalById(String id) {

        return legalPersonService
                .findLegalById(id)
                .map(IPersonMapper.INSTANCE::toLegalPersonResponse);
    }

    @Override //TODO ERRO AQUI
    @SecuredDelegate(scopes = {USER_WRITE_SCOPE})
    public Mono<Void> saveLegalPerson(LegalPersonRequest legalPerson) {

        return Mono
                .just(legalPerson)
                .map(IPersonMapper.INSTANCE::toLegalPersonByRequest)
                .flatMap(legalPersonService::saveLegalPerson);
    }

    @Override
    @SecuredDelegate(scopes = {ADMIN_WRITE_SCOPE})
    public Mono<Void> savePartnerToLegalPerson(String cnpj, String partnerCPF) {

        return null;
    }

    @Override
    @SecuredDelegate(scopes = {ADMIN_WRITE_SCOPE})
    public Mono<Void> updateLegalPerson(String cnpj, LegalPersonRequest legalPerson) {

        return Mono
                .just(legalPerson)
                .map(IPersonMapper.INSTANCE::toLegalPersonByRequest)
                .flatMap(response -> legalPersonService.updateLegalPerson(cnpj, response));
    }

    @Override
    @SecuredDelegate(scopes = {ADMIN_WRITE_SCOPE})
    public Mono<Void> deleteLegalPerson(String companyCNPJ) {

        return null;
    }

    @Override
    @SecuredDelegate(scopes = {ADMIN_WRITE_SCOPE})
    public Mono<Void> deletePartnerByLegalPerson(String companyCNPJ, String partnerCPF) {

        return null;
    }

    @Override
    @SecuredDelegate(scopes = {USER_READ_SCOPE})
    public Flux<NaturalPersonResponse> findAllNaturals() {

        return naturalPersonService
                .findAllNaturals()
                .map(IPersonMapper.INSTANCE::toNaturalPersonResponse);
    }

    @Override
    @SecuredDelegate(scopes = {USER_READ_SCOPE})
    public Mono<NaturalPersonResponse> findNaturalByCPF(String cpf) {

        return naturalPersonService
                .findNaturalByCPF(cpf)
                .map(IPersonMapper.INSTANCE::toNaturalPersonResponse);
    }

    @Override
    @SecuredDelegate(scopes = {USER_READ_SCOPE})
    public Mono<NaturalPersonResponse> findNaturalByID(String id) {

        return naturalPersonService
                .findNaturalById(id)
                .map(IPersonMapper.INSTANCE::toNaturalPersonResponse);
    }

    @Override //TODO ERRO AQUI
    @SecuredDelegate(scopes = {USER_WRITE_SCOPE})
    public Mono<Void> saveNaturalPerson(NaturalPersonRequest naturalPerson) {

        return Mono
                .just(naturalPerson)
                .map(IPersonMapper.INSTANCE::toNaturalPersonByRequest)
                .flatMap(naturalPersonService::saveNaturalPerson);
    }

    @Override
    @SecuredDelegate(scopes = {ADMIN_WRITE_SCOPE})
    public Mono<Void> updateNaturalPerson(String cpf, NaturalPersonRequest naturalPerson) {

        return Mono
                .just(naturalPerson)
                .map(IPersonMapper.INSTANCE::toNaturalPersonByRequest)
                .flatMap(self -> naturalPersonService.updateNaturalPerson(self, cpf));
    }

    @Override
    @SecuredDelegate(scopes = {ADMIN_WRITE_SCOPE})
    public Mono<Void> deleteNaturalPerson(String cpf) {

        return null;
    }
}
package com.carbonaro.ReactiveSimplifiedPicPay.services;

import com.carbonaro.ReactiveSimplifiedPicPay.api.requests.LegalPersonFilterRequest;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.LegalPerson;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.NaturalPerson;
import com.carbonaro.ReactiveSimplifiedPicPay.repositories.LegalPersonRepository;
import com.carbonaro.ReactiveSimplifiedPicPay.services.exceptions.BadRequestException;
import com.carbonaro.ReactiveSimplifiedPicPay.services.exceptions.EmptyException;
import com.carbonaro.ReactiveSimplifiedPicPay.services.exceptions.NotFoundException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@Slf4j
@Service
@RequiredArgsConstructor
public class LegalPersonService {

    private final LegalPersonRepository legalPersonRepository;
    private final NaturalPersonService naturalPersonService;

    public Mono<Void> saveLegalPerson(LegalPerson legalPerson) {

        return Mono
                .just(legalPerson)
                .flatMap(legalPersonRepository::save)
                .doOnSuccess(person -> log.info("New LegalPerson was persisted with success!"))
                .doOnError(errorResponse -> Mono.error(new Exception(errorResponse.getMessage())));
    }

    public Mono<LegalPerson> updateLegalPerson(String cnpj, LegalPerson legalPerson) {

        log.info("LegalPerson | Updating company for CNPJ: {}", cnpj);
        return findLegalByCNPJ(cnpj)
                .zipWith(validateLegalPersonFields(legalPerson))
                .flatMap(legalPersonRepository::update);
    }

    public Mono<Void> savePartner(String cnpj, String partnerCPF) {

        log.info("LegalPerson | Saving new partner for company: {}", cnpj);
        return Mono
                .zip(this.findLegalByCNPJ(cnpj), naturalPersonService.findNaturalByCPF(partnerCPF))
                .flatMap(this::savePartner);
    }
    private Mono<Void> savePartner(Tuple2<LegalPerson, NaturalPerson> tuple) {

        return Mono
                .just(tuple)
                .flatMap(self -> {

                    if (self.getT1().getPartners().stream().anyMatch(it -> it.getCpf().equals(tuple.getT2().getCpf()))) {
                        return Mono.error(new DataIntegrityViolationException("Already have this partner in the company"));
                    } else {
                        self.getT1().getPartners().add(self.getT2());
                    }

                    return saveLegalPerson(self.getT1());
                });
    }

    public Mono<Void> deletePartner(String cnpj, String partnerCNPJ) {

        log.info("LegalPerson | Deleting partner: {}, from company: {}", partnerCNPJ, cnpj);
        return Mono
                .zip(this.findLegalByCNPJ(cnpj), naturalPersonService.findNaturalByCPF(partnerCNPJ))
                .flatMap(this::deletePartner);
    }
    private Mono<Void> deletePartner(Tuple2<LegalPerson, NaturalPerson> tuple) {

        return Mono
                .just(tuple)
                .flatMap(self -> self.getT1().getPartners().removeIf(it -> it.getCpf().equals(self.getT2().getCpf()))
                        ? this.saveLegalPerson(tuple.getT1())
                        : Mono.error(new NotFoundException("Don't exist any partner with that CPF in this company.")));
    }

    public Mono<Page<LegalPerson>> findAllLegals(Pageable page, LegalPersonFilterRequest filterRequest) {

        return legalPersonRepository
                .findAll(page, filterRequest)
                .switchIfEmpty(Mono.error(new EmptyException()))
                .doOnError(errorResponse -> Flux.error(new Exception(errorResponse.getMessage())));
    }

    public Mono<LegalPerson> findLegalById(String id) {

        return legalPersonRepository
                .findById(id)
                .switchIfEmpty(Mono.error(new EmptyException()))
                .doOnError(errorResponse -> Mono.error(new Exception(errorResponse.getMessage())));
    }

    public Mono<LegalPerson> findLegalByCNPJ(String cnpj) {

        return legalPersonRepository
                .findByCnpj(cnpj)
                .switchIfEmpty(Mono.error(new EmptyException()))
                .doOnError(errorResponse -> Mono.error(new Exception(errorResponse.getMessage())));
    }

    private Mono<LegalPerson> validateLegalPersonFields(LegalPerson legalPerson) {

        return Stream
                .concat(
                        Arrays.stream(legalPerson.getClass().getDeclaredFields()),
                        Arrays.stream(legalPerson.getClass().getSuperclass().getDeclaredFields()))
                .filter(field -> {
                    try {
                        field.setAccessible(true);
                        return Objects.nonNull(field.get(legalPerson)) && !field.get(legalPerson).toString().isEmpty() && field.get(legalPerson).toString().length() != 1;
                    } catch (IllegalAccessException e) {
                        throw new DataIntegrityViolationException(e.getMessage());
                    }})
                .toList()
                .isEmpty()

                ? Mono.error(new BadRequestException())
                : Mono.just(legalPerson);
    }

    public Mono<Void> deleteLegalPerson(String companyCNPJ) {

        return legalPersonRepository
                .deleteByCnpj(companyCNPJ);
    }

    public Mono<Void> deposit(LegalPerson legalPerson, BigDecimal amount) {

        return Mono.just(legalPerson)
                .flatMap(self -> {
                    self.setBalance(self.getBalance().add(amount));
                    return legalPersonRepository.save(self);
                })
                .doOnError(errorResponse -> Mono.error(new Exception(errorResponse.getMessage())));
    }

}
package com.carbonaro.ReactiveSimplifiedPicPay.services;

import com.carbonaro.ReactiveSimplifiedPicPay.api.requests.person.LegalPersonFilterRequest;
import com.carbonaro.ReactiveSimplifiedPicPay.api.responses.PageResponse;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.LegalPerson;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.NaturalPerson;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.mappers.PageMapper;
import com.carbonaro.ReactiveSimplifiedPicPay.repositories.LegalPersonRepository;
import com.carbonaro.ReactiveSimplifiedPicPay.services.exceptions.EmptyReturnException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

import static com.carbonaro.ReactiveSimplifiedPicPay.AppConstants.GENERAL_WARNING_EMPTY;
import static com.carbonaro.ReactiveSimplifiedPicPay.AppConstants.LEGAL_DELETE_PARTNER_NOT_FOUND_ERROR;

@Slf4j
@Service
@RequiredArgsConstructor
public class LegalPersonService {

    private final LegalPersonRepository legalPersonRepository;
    private final NaturalPersonService naturalPersonService;
    private final PageMapper<LegalPerson> pageMapper;

    public Mono<Void> saveLegalPerson(LegalPerson legalPerson) {

        return Mono
                .just(legalPerson)
                .flatMap(this::validateLegalPerson)
                .flatMap(legalPersonRepository::save)
                .doOnSuccess(person -> log.info("New LegalPerson was persisted with success!"))
                .doOnError(errorResponse -> Mono.error(new Exception(errorResponse.getMessage())))
                .then();
    }

    public Mono<Void> deleteLegal(String companyCNPJ) {

        return findLegalByCNPJ(companyCNPJ)
                .map(company -> legalPersonRepository.deleteByCnpj(company.getCnpj()).subscribe())
                .doOnSuccess(unused -> log.info("Company with CNPJ: {}, was deleted with success!", companyCNPJ))
                .then();
    }

    public Mono<Void> updateLegalPerson(String cnpj, LegalPerson updatedLegalPerson) {

        log.info("LegalPerson | Updating Legal for CNPJ: {}", cnpj);
        return Mono.just(updatedLegalPerson)
                .flatMap(this::validateFields)
                .zipWith(findLegalByCNPJ(cnpj))
                .flatMap(legalPersonRepository::update)
                .then();
    }
    private Mono<LegalPerson> validateFields(LegalPerson legalPerson) {

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

                ? Mono.error(new Exception())
                : Mono.just(legalPerson);
    }

    //TODO REFAZER SAVEPARTNER
    public Mono<Void> savePartner(String cnpj, String partnerCPF) {

        log.info("LegalPerson | Saving new partner for company: {}", cnpj);
        return Mono
                .zip(this.findLegalByCNPJ(cnpj), naturalPersonService.findNaturalByCPF(partnerCPF))
                .flatMap(this::savePartner)
                .then();
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

                    return this.saveLegalPerson(self.getT1());
                });
    }

    public Mono<Void> deletePartner(String partnerCPF) {

        return null;
    }
    private Mono<LegalPerson> removePartner(LegalPerson company, String partnerCPF) {

        return company
                .getPartners()
                .removeIf(partner -> partner.getCpf().equals(partnerCPF))
                ? Mono.just(company)
                : Mono.error(new EmptyReturnException(LEGAL_DELETE_PARTNER_NOT_FOUND_ERROR));
    }

    public Mono<PageResponse<LegalPerson>> listAll(Pageable page, LegalPersonFilterRequest filterRequest) {

        return legalPersonRepository
                .listAll(page, filterRequest)
                .switchIfEmpty(Mono.error(new EmptyReturnException(GENERAL_WARNING_EMPTY)))
                .map(pageMapper::toPageResponse)
                .doOnError(errorResponse -> Mono.error(new Exception(errorResponse.getMessage())));
    }

    public Mono<LegalPerson> findLegalById(String id) {

        return legalPersonRepository
                .findById(id)
                .switchIfEmpty(Mono.error(new EmptyReturnException(GENERAL_WARNING_EMPTY)))
                .doOnError(errorResponse -> Mono.error(new Exception(errorResponse.getMessage())));
    }

    public Mono<LegalPerson> findLegalByCNPJ(String cnpj) {

        return legalPersonRepository
                .findByCnpj(cnpj)
                .switchIfEmpty(Mono.error(new EmptyReturnException(GENERAL_WARNING_EMPTY)))
                .doOnError(errorResponse -> Mono.error(new Exception(errorResponse.getMessage())));
    }

    private Mono<LegalPerson> validateLegalPerson(LegalPerson legalPerson) {

        String onlyNumbers = "\\d+";
        return Mono
                .just(legalPerson)
                .flatMap(unused -> legalPersonRepository
                            .findByCnpj(legalPerson.getCnpj())
                            .hasElement()
                            .flatMap(hasValue -> Boolean.FALSE.equals(hasValue) ? Mono.just(legalPerson) : Mono.error(new DataIntegrityViolationException("Already have a LegalPerson with that CNPJ.")))
                )
                .flatMap(unused -> legalPerson.getCnpj().matches(onlyNumbers) && legalPerson.getCnpj().length() == 14
                            ? Mono.just(legalPerson)
                            : Mono.error(new DataIntegrityViolationException("LegalPerson CNPJ contains more than 14 numbers or don't have only numbers"))
                );
    }

}
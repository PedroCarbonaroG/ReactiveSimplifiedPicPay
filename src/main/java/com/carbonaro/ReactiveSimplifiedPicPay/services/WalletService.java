package com.carbonaro.ReactiveSimplifiedPicPay.services;

import static com.carbonaro.ReactiveSimplifiedPicPay.AppConstants.GENERAL_INVALID_DOCUMENT_FORMAT;

import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.LegalPerson;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.NaturalPerson;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.Person;
import com.carbonaro.ReactiveSimplifiedPicPay.services.exceptions.BadRequestException;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final NaturalPersonService naturalPersonService;
    private final LegalPersonService legalPersonService;

    public Mono<Void> deposit(String document, BigDecimal amount) {

        return Mono.just(document)
                .flatMap(this::isValidDocument)
                .flatMap(this::checkAccountType)
                .flatMap(person -> makeDeposit(person, amount));
    }
    private Mono<String> isValidDocument(String document) {

        return Mono.just(document)
                .map(self -> {
                    if (document.length() != 11 && document.length() != 14) {
                        throw new BadRequestException(GENERAL_INVALID_DOCUMENT_FORMAT);
                    }
                    return self;
                });
    }
    private Mono<Void> makeDeposit(Person person, BigDecimal amount) {

        return Mono.just(person)
                .flatMap(self ->
                        self instanceof NaturalPerson
                                ? naturalPersonService.deposit((NaturalPerson) self, amount)
                                : legalPersonService.deposit((LegalPerson) self, amount));
    }

    public Mono<BigDecimal> consultBalance(String document) {

        return Mono.just(document)
                .flatMap(this::checkAccountType)
                .map(Person::getBalance);
    }

    private Mono<? extends Person> checkAccountType(String document) {
        return (document.length() == 11)
                ? naturalPersonService.findNaturalByCPF(document)
                : legalPersonService.findLegalByCNPJ(document);
    }

}

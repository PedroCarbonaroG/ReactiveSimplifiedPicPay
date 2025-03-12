package com.carbonaro.ReactiveSimplifiedPicPay.services;

import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.LegalPerson;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.NaturalPerson;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.Person;
import com.carbonaro.ReactiveSimplifiedPicPay.services.exceptions.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.math.BigDecimal;

import static com.carbonaro.ReactiveSimplifiedPicPay.AppConstants.WALLET_INVALID_DOCUMENT_FORMAT;
import static com.carbonaro.ReactiveSimplifiedPicPay.AppConstants.WALLET_NEGATIVE_AMOUNT_FOR_DEPOSIT;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final NaturalPersonService naturalPersonService;
    private final LegalPersonService legalPersonService;

    public Mono<Void> deposit(String document, BigDecimal amount) {

        return this.checkDepositValue(amount, document)
                .flatMap(this::isValidDocument)
                .flatMap(this::checkAccountType)
                .flatMap(person -> makeDeposit(person, amount));
    }
    private Mono<String> checkDepositValue(BigDecimal amount, String document) {

        return Mono.just(amount)
                .map(self -> {
                    if (self.signum() == -1) {
                        throw new BadRequestException(WALLET_NEGATIVE_AMOUNT_FOR_DEPOSIT);
                    }
                    return self;
                })
                .map(validatedAmount -> document);
    }
    private Mono<String> isValidDocument(String document) {

        return Mono.just(document)
                .map(self -> {
                    if (document.length() != 11 && document.length() != 14) {
                        throw new BadRequestException(WALLET_INVALID_DOCUMENT_FORMAT);
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

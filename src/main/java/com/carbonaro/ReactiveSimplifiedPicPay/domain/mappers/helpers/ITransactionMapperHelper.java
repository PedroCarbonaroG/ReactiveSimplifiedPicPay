package com.carbonaro.ReactiveSimplifiedPicPay.domain.mappers.helpers;

import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.Person;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.Transaction;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.mappers.IPersonMapper;
import com.carbonaro.ReactiveSimplifiedPicPay.api.responses.person.PersonResponse;
import com.carbonaro.ReactiveSimplifiedPicPay.services.LegalPersonService;
import com.carbonaro.ReactiveSimplifiedPicPay.services.NaturalPersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ITransactionMapperHelper {

    private final NaturalPersonService naturalPersonService;
    private final LegalPersonService legalPersonService;

    public Mono<PersonResponse> getSenderToMapper(Transaction transaction) {

        return naturalPersonService
                .findNaturalByCPF(transaction.getSenderDocument())
                .map(IPersonMapper.INSTANCE::toNaturalPersonResponse);
    }

    public Mono<PersonResponse> getReceiverToMapper(Transaction transaction) {

        return (transaction.getReceiverDocument().length() == 11)
                ? naturalPersonService.findNaturalByCPF(transaction.getReceiverDocument())
                    .map(IPersonMapper.INSTANCE::toNaturalPersonResponse)

                : legalPersonService.findLegalByCNPJ(transaction.getReceiverDocument())
                    .map(IPersonMapper.INSTANCE::toLegalPersonTransactionResponse);
    }

    public Mono<? extends Person> getSenderForTransaction(Transaction transaction) {

        return (transaction.getSenderDocument().length() == 11)
                ? naturalPersonService.findNaturalByCPF(transaction.getSenderDocument())
                : legalPersonService.findLegalByCNPJ(transaction.getReceiverDocument());
    }

    public Mono<? extends Person> getReceiverForTransaction(Transaction transaction) {

        return (transaction.getReceiverDocument().length() == 11)
                ? naturalPersonService.findNaturalByCPF(transaction.getReceiverDocument())
                : legalPersonService.findLegalByCNPJ(transaction.getReceiverDocument());
    }
}

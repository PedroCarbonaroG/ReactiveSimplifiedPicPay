package com.carbonaro.ReactiveSimplifiedPicPay.domain.mappers.helpers;

import com.carbonaro.ReactiveSimplifiedPicPay.domain.mappers.IPersonMapper;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.responses.person.PersonResponse;
import com.carbonaro.ReactiveSimplifiedPicPay.services.LegalPersonService;
import com.carbonaro.ReactiveSimplifiedPicPay.services.NaturalPersonService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class ITransactionMapperHelper {

    private final NaturalPersonService naturalPersonService;
    private final LegalPersonService legalPersonService;

    public Mono<PersonResponse> getSender(String senderDocument) {

        return naturalPersonService
                .findNaturalByCPF(senderDocument)
                .map(IPersonMapper.INSTANCE::toNaturalPersonResponse);
    }

    public Mono<PersonResponse> getReceiver(String receiverDocument) {

        return (receiverDocument.length() == 11)
                ? naturalPersonService
                    .findNaturalByCPF(receiverDocument)
                    .map(IPersonMapper.INSTANCE::toNaturalPersonResponse)
                : legalPersonService
                    .findLegalByCNPJ(receiverDocument)
                    .map(IPersonMapper.INSTANCE::toLegalPersonTransactionResponse);
    }
}

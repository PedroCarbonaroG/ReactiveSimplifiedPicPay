package com.carbonaro.ReactiveSimplifiedPicPay.services.helper;

import com.carbonaro.ReactiveSimplifiedPicPay.domain.mappers.ITransactionMapper;
import com.carbonaro.ReactiveSimplifiedPicPay.services.LegalPersonService;
import com.carbonaro.ReactiveSimplifiedPicPay.services.NaturalPersonService;
import com.carbonaro.ReactiveSimplifiedPicPay.services.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class ExportingBuilderExcelHelper {

    private final ITransactionMapper transactionMapper;
    private final TransactionService transactionService;
    private final LegalPersonService legalPersonService;

    private ExportingServiceHelper exportingServiceHelper;
    private final NaturalPersonService naturalPersonService;

    public Mono<byte[]> buildExcel(Object object) {

        return Mono.just(object)
                .map(this::initialBuilder);
    }
    
    private byte[] initialBuilder(Object object) {

        return null;
    }

}
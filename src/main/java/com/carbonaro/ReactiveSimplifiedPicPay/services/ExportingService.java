package com.carbonaro.ReactiveSimplifiedPicPay.services;

import com.carbonaro.ReactiveSimplifiedPicPay.api.requests.person.LegalPersonFilterRequest;
import com.carbonaro.ReactiveSimplifiedPicPay.api.requests.person.NaturalPersonFilterRequest;
import com.carbonaro.ReactiveSimplifiedPicPay.api.requests.transaction.TransactionFilterRequest;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.enums.FileTypeEnum;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.mappers.ITransactionMapper;
import com.carbonaro.ReactiveSimplifiedPicPay.services.helper.ExportingBuilderExcelHelper;
import com.carbonaro.ReactiveSimplifiedPicPay.services.helper.ExportingBuilderPdfHelper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

@Service
@AllArgsConstructor
public class ExportingService {

    private final ExportingBuilderPdfHelper pdfBuilder;
    private final ExportingBuilderExcelHelper excelBuilder;
    private final ITransactionMapper transactionMapper;
    private final TransactionService transactionService;
    private final NaturalPersonService naturalPersonService;
    private final LegalPersonService legalPersonService;

    public Mono<byte[]> getTransactionsExtraction(TransactionFilterRequest filterRequest, FileTypeEnum fileTypeEnum) {

        var page = PageRequest.of(0, 20);
        return transactionService.findAllTransactions(page, filterRequest)
                .map(transactionMapper::toPageTransactionResponse)
                .flatMap(transactionResponse -> buildResponseByFileType(transactionResponse, fileTypeEnum));
    }

    public Mono<byte[]> getPersonsToExtraction(FileTypeEnum fileTypeEnum) {

        var page = PageRequest.of(0, 20);
        var naturalFilter = NaturalPersonFilterRequest.builder().build();
        var legalFilter = LegalPersonFilterRequest.builder().build();

        return Mono.just(Tuples.of(naturalPersonService.findAllNaturals(page, naturalFilter), legalPersonService.findAllLegals(page, legalFilter)))
                .flatMap(self -> buildResponseByFileType(self, fileTypeEnum));
    }

    private <T> Mono<byte[]> buildResponseByFileType(T object, FileTypeEnum fileTypeEnum) {

        return switch (fileTypeEnum) {
            case EXCEL -> excelBuilder.buildExcel(object);
            case PDF -> pdfBuilder.buildPdf(object);
        };
    }

}

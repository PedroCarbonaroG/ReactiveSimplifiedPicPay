package com.carbonaro.ReactiveSimplifiedPicPay.services;

import com.carbonaro.ReactiveSimplifiedPicPay.api.requests.TransactionFilterRequest;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.enums.FileTypeEnum;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.mappers.ITransactionMapper;
import com.carbonaro.ReactiveSimplifiedPicPay.services.helper.ExportingBuilderExcelHelper;
import com.carbonaro.ReactiveSimplifiedPicPay.services.helper.ExportingBuilderPdfHelper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class ExportingService {

    private final ExportingBuilderPdfHelper pdfBuilder;
    private final ExportingBuilderExcelHelper excelBuilder;
    private final ITransactionMapper transactionMapper;
    private final TransactionService transactionService;
    private final LegalPersonService legalPersonService;
    private final NaturalPersonService naturalPersonService;

    public Mono<byte[]> getTransactionsExtraction(TransactionFilterRequest filterRequest, FileTypeEnum fileTypeEnum) {

        var page = PageRequest.of(0, 20);
        return transactionService.findAllTransactions(page, filterRequest)
                .map(transactionMapper::toPageTransactionResponse)
                .flatMap(transactionResponse -> buildResponseByFileType(transactionResponse, fileTypeEnum));
    }

    public Mono<byte[]> getPersonsToExtraction(FileTypeEnum fileTypeEnum) {

        return null;
    }

    private Mono<byte[]> buildResponseByFileType(Object object, FileTypeEnum fileTypeEnum) {

        return switch (fileTypeEnum) {
            case EXCEL -> excelBuilder.buildExcel(object);
            case PDF -> pdfBuilder.buildPdf(object);
        };
    }

}

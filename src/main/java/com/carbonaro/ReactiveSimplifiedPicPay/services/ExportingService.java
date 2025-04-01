package com.carbonaro.ReactiveSimplifiedPicPay.services;

import com.carbonaro.ReactiveSimplifiedPicPay.api.requests.person.LegalPersonFilterRequest;
import com.carbonaro.ReactiveSimplifiedPicPay.api.requests.person.NaturalPersonFilterRequest;
import com.carbonaro.ReactiveSimplifiedPicPay.api.requests.transaction.TransactionFilterRequest;
import com.carbonaro.ReactiveSimplifiedPicPay.api.responses.PageResponse;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.enums.FileTypeEnum;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.mappers.IPersonMapper;
import com.carbonaro.ReactiveSimplifiedPicPay.services.helper.ExportingBuilderExcelHelper;
import com.carbonaro.ReactiveSimplifiedPicPay.services.helper.ExportingBuilderPdfHelper;
import java.util.List;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExportingService {

    private final ExportingBuilderPdfHelper pdfBuilder;
    private final ExportingBuilderExcelHelper excelBuilder;
    private final TransactionService transactionService;
    private final NaturalPersonService naturalPersonService;
    private final LegalPersonService legalPersonService;

    public Mono<byte[]> getTransactionsExtraction(TransactionFilterRequest filterRequest, FileTypeEnum fileTypeEnum) {

        return getAllRecords(pageRequest -> transactionService.findAllTransactions(pageRequest, filterRequest))
                .map(allTransactions -> buildResponseByFileType(allTransactions, fileTypeEnum));
    }

    public Mono<byte[]> getNaturalsToExtraction(NaturalPersonFilterRequest filter, FileTypeEnum fileTypeEnum) {

        return getAllRecords(pageRequest -> naturalPersonService.findAllNaturals(pageRequest, filter)
                .map(IPersonMapper.INSTANCE::toPageResponseNaturalPersonResponse))
                .map(allNaturals -> buildResponseByFileType(allNaturals, fileTypeEnum));
    }

    public Mono<byte[]> getLegalsToExtraction(LegalPersonFilterRequest filter, FileTypeEnum fileTypeEnum) {

        return getAllRecords(pageRequest -> legalPersonService.findAllLegals(pageRequest, filter)
                .map(IPersonMapper.INSTANCE::toPageResponseLegalPersonResponse))
                .map(allLegals -> buildResponseByFileType(allLegals, fileTypeEnum));
    }

    private <T> Mono<List<T>> getAllRecords(Function<PageRequest, Mono<PageResponse<T>>> fetchFunction) {
        int size = 20;

        return Flux
                .range(0, Integer.MAX_VALUE)
                .concatMap(page -> {
                    PageRequest pageRequest = PageRequest.of(page, size);
                    return fetchFunction.apply(pageRequest)
                            .filter(pageResponse -> pageResponse.getContent() != null && !pageResponse.getContent().isEmpty())
                            .map(PageResponse::getContent)
                            .defaultIfEmpty(List.of());})
                .takeWhile(list -> !list.isEmpty())
                .flatMap(Flux::fromIterable)
                .collectList();
    }

    private <T> byte[] buildResponseByFileType(List<T> report, FileTypeEnum fileTypeEnum) {

        return switch (fileTypeEnum) {
            case EXCEL -> excelBuilder.buildExcel(report);
            case PDF -> pdfBuilder.buildPdf(report);
        };
    }

}

package com.carbonaro.ReactiveSimplifiedPicPay.api.impl;

import com.carbonaro.ReactiveSimplifiedPicPay.api.IExportingAPI;
import com.carbonaro.ReactiveSimplifiedPicPay.api.requests.person.LegalPersonFilterRequest;
import com.carbonaro.ReactiveSimplifiedPicPay.api.requests.person.NaturalPersonFilterRequest;
import com.carbonaro.ReactiveSimplifiedPicPay.api.requests.transaction.TransactionFilterRequest;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.LegalPerson;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.NaturalPerson;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.Transaction;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.enums.FileTypeEnum;
import com.carbonaro.ReactiveSimplifiedPicPay.services.ExportingService;
import com.carbonaro.ReactiveSimplifiedPicPay.services.helper.ExportingServiceHelper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import static com.carbonaro.ReactiveSimplifiedPicPay.AppConstants.ADMIN_SCOPE;

@Slf4j
@RestController
@AllArgsConstructor
public class ExportingImpl implements IExportingAPI {

    private final ExportingService exportingService;

    @Override
    @PreAuthorize(value = ADMIN_SCOPE)
    public Mono<ResponseEntity<byte[]>> transactionsToExtraction(TransactionFilterRequest filter, FileTypeEnum fileType) {

        log.info("Exporting transactions to extraction report for {} format", fileType);
        return exportingService.getTransactionsExtraction(filter, fileType)
                .flatMap(bytesToExport -> ExportingServiceHelper.exportingResponse(bytesToExport, fileType, Transaction.class));
    }

    @Override
    @PreAuthorize(value = ADMIN_SCOPE)
    public Mono<ResponseEntity<byte[]>> naturalsToExtraction(NaturalPersonFilterRequest filter, FileTypeEnum fileType) {

        log.info("Exporting naturals to extraction report for {} format", fileType);
        return exportingService.getNaturalsToExtraction(filter, fileType)
                .flatMap(bytesToExport -> ExportingServiceHelper.exportingResponse(bytesToExport, fileType, NaturalPerson.class));
    }

    @Override
    @PreAuthorize(value = ADMIN_SCOPE)
    public Mono<ResponseEntity<byte[]>> legalsToExtraction(LegalPersonFilterRequest filter, FileTypeEnum fileType) {

        log.info("Exporting legals to extraction report for {} format", fileType);
        return exportingService.getLegalsToExtraction(filter, fileType)
                .flatMap(bytesToExport -> ExportingServiceHelper.exportingResponse(bytesToExport, fileType, LegalPerson.class));
    }

}

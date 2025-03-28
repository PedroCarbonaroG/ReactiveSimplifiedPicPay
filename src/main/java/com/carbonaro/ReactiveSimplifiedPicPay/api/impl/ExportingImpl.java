package com.carbonaro.ReactiveSimplifiedPicPay.api.impl;

import com.carbonaro.ReactiveSimplifiedPicPay.api.IExportingAPI;
import com.carbonaro.ReactiveSimplifiedPicPay.api.requests.transaction.TransactionFilterRequest;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.Person;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.Transaction;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.enums.FileTypeEnum;
import com.carbonaro.ReactiveSimplifiedPicPay.services.ExportingService;
import com.carbonaro.ReactiveSimplifiedPicPay.services.helper.ExportingServiceHelper;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import static com.carbonaro.ReactiveSimplifiedPicPay.AppConstants.ADMIN_SCOPE;

@RestController
@AllArgsConstructor
public class ExportingImpl implements IExportingAPI {

    private final ExportingService exportingService;

    @Override
    @PreAuthorize(value = ADMIN_SCOPE)
    public Mono<ResponseEntity<byte[]>> getTransactionsExtraction(TransactionFilterRequest filterRequest, FileTypeEnum fileType) {

        return exportingService.getTransactionsExtraction(filterRequest, fileType)
                .flatMap(bytesToExport -> ExportingServiceHelper.exportingResponse(bytesToExport, fileType, Transaction.class));
    }

    @Override
    @PreAuthorize(value = ADMIN_SCOPE)
    public Mono<ResponseEntity<byte[]>> getPersonsToExtraction(FileTypeEnum fileType) {

        return exportingService.getPersonsToExtraction(fileType)
                .flatMap(bytesToExport -> ExportingServiceHelper.exportingResponse(bytesToExport, fileType, Person.class));
    }

}

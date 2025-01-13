package com.carbonaro.ReactiveSimplifiedPicPay.api;

import com.carbonaro.ReactiveSimplifiedPicPay.api.annotations.route_params.TransactionFilterRequestAsQueryParam;
import com.carbonaro.ReactiveSimplifiedPicPay.api.requests.transaction.TransactionFilterRequest;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.enums.FileTypeEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@Tag(name = "Exporting API - Data system export")
@RequestMapping(value = "/extraction", produces = MediaType.APPLICATION_JSON_VALUE)
public interface IExportingAPI {

    @Operation()
    @GetMapping("/transactions")
    @TransactionFilterRequestAsQueryParam
    Mono<ResponseEntity<byte[]>> getTransactionsExtraction(TransactionFilterRequest filterRequest, FileTypeEnum fileType);

    @Operation()
    @GetMapping("/persons")
    Mono<ResponseEntity<byte[]>> getPersonsToExtraction(FileTypeEnum fileType);

}

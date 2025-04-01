package com.carbonaro.ReactiveSimplifiedPicPay.api;

import com.carbonaro.ReactiveSimplifiedPicPay.api.annotations.route_description.GetPersonsToExtractionRouteDescription;
import com.carbonaro.ReactiveSimplifiedPicPay.api.annotations.route_description.GetTransactionsExtractionRouteDescription;
import com.carbonaro.ReactiveSimplifiedPicPay.api.annotations.route_params.TransactionFilterRequestAsQueryParam;
import com.carbonaro.ReactiveSimplifiedPicPay.api.requests.transaction.TransactionFilterRequest;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.enums.FileTypeEnum;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@Tag(name = "Exporting API - Data system export")
@RequestMapping(value = "/extraction", produces = MediaType.APPLICATION_JSON_VALUE)
public interface IExportingAPI {

    @GetMapping("/transactions")
    @TransactionFilterRequestAsQueryParam
    @GetTransactionsExtractionRouteDescription
    Mono<ResponseEntity<byte[]>> getTransactionsExtraction(TransactionFilterRequest filterRequest, FileTypeEnum fileType);

    @GetMapping("/persons")
    @GetPersonsToExtractionRouteDescription
    Mono<ResponseEntity<byte[]>> getPersonsToExtraction(FileTypeEnum fileType);

}

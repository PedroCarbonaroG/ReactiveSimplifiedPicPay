package com.carbonaro.ReactiveSimplifiedPicPay.api;

import com.carbonaro.ReactiveSimplifiedPicPay.api.annotations.route_params.LegalPersonFilterRequestAsQueryParam;
import com.carbonaro.ReactiveSimplifiedPicPay.api.annotations.route_params.NaturalPersonFilterRequestAsQueryParam;
import com.carbonaro.ReactiveSimplifiedPicPay.api.annotations.route_params.TransactionFilterRequestAsQueryParam;
import com.carbonaro.ReactiveSimplifiedPicPay.api.requests.person.LegalPersonFilterRequest;
import com.carbonaro.ReactiveSimplifiedPicPay.api.requests.person.NaturalPersonFilterRequest;
import com.carbonaro.ReactiveSimplifiedPicPay.api.requests.transaction.TransactionFilterRequest;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.enums.FileTypeEnum;
import io.swagger.v3.oas.annotations.Parameter;
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
    @TransactionsToExtractionRouteDescription
    Mono<ResponseEntity<byte[]>> transactionsToExtraction(
            @Parameter(hidden = true) TransactionFilterRequest filter,
            @Parameter FileTypeEnum fileType);

    @GetMapping("/person/naturals")
    @NaturalsToExtractionRouteDescription
    @NaturalPersonFilterRequestAsQueryParam
    Mono<ResponseEntity<byte[]>> naturalsToExtraction(
            @Parameter(hidden = true) NaturalPersonFilterRequest filter,
            @Parameter FileTypeEnum fileType);

    @GetMapping("/person/legals")
    @LegalsToExtractionRouteDescription
    @LegalPersonFilterRequestAsQueryParam
    Mono<ResponseEntity<byte[]>> legalsToExtraction(
            @Parameter(hidden = true) LegalPersonFilterRequest filter,
            @Parameter FileTypeEnum fileType);
}

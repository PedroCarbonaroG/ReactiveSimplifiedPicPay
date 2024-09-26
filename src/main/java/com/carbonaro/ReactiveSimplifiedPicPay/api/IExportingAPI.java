package com.carbonaro.ReactiveSimplifiedPicPay.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@Tag(name = "Exporting API - Data export from Person")
@RequestMapping(value = "/extraction", produces = MediaType.APPLICATION_JSON_VALUE)
public interface IExportingAPI {

    @Operation()
    @GetMapping("/persons/pdf")
    Mono<ResponseEntity<byte[]>> getPersonPdfExtraction();

    @Operation()
    @GetMapping("/persons/excel")
    Mono<ResponseEntity<byte[]>> getPersonExcelExtraction();

    @Operation()
    @GetMapping("/transactions/pdf")
    Mono<ResponseEntity<byte[]>> getTransactionPdfExtraction();

    @Operation()
    @GetMapping("/transactions/excel")
    Mono<ResponseEntity<byte[]>> getTransactionExcelExtraction();

}

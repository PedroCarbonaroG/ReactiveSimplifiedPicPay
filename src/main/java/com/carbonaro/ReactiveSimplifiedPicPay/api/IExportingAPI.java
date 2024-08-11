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
    @GetMapping("/pdf")
    Mono<ResponseEntity<byte[]>> getPdfExtraction();

    @Operation()
    @GetMapping("/excel")
    Mono<ResponseEntity<byte[]>> getExcelExtraction();
}

package com.carbonaro.ReactiveSimplifiedPicPay.api;

import com.carbonaro.ReactiveSimplifiedPicPay.api.annotations.RouteDescriptions.FindAllTransactionsRouteDescription;
import com.carbonaro.ReactiveSimplifiedPicPay.api.annotations.RouteDescriptions.SaveTransactionRouteDescription;
import com.carbonaro.ReactiveSimplifiedPicPay.api.annotations.RouteParams.TransactionRequestAsQueryParam;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.requests.transaction.TransactionRequest;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.responses.transaction.TransactionResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Tag(name = "Transaction API - Person transactions handler")
@RequestMapping(value = "/transaction", produces = MediaType.APPLICATION_JSON_VALUE)
public interface ITransactionAPI {

    @GetMapping("/list-all")
    @FindAllTransactionsRouteDescription
    Flux<TransactionResponse> findAllTransactions();

    @PostMapping
    @TransactionRequestAsQueryParam
    @SaveTransactionRouteDescription
    Mono<Void> saveTransaction(@Parameter(hidden = true) TransactionRequest transaction);

}

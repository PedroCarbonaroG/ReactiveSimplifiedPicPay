package com.carbonaro.ReactiveSimplifiedPicPay.api;

import com.carbonaro.ReactiveSimplifiedPicPay.api.annotations.route_description.FindAllTransactionsRouteDescription;
import com.carbonaro.ReactiveSimplifiedPicPay.api.annotations.route_description.SaveTransactionRouteDescription;
import com.carbonaro.ReactiveSimplifiedPicPay.api.annotations.route_params.TransactionFilterRequestAsQueryParam;
import com.carbonaro.ReactiveSimplifiedPicPay.api.annotations.route_params.TransactionRequestAsQueryParam;
import com.carbonaro.ReactiveSimplifiedPicPay.api.requests.TransactionFilterRequest;
import com.carbonaro.ReactiveSimplifiedPicPay.api.requests.TransactionRequest;
import com.carbonaro.ReactiveSimplifiedPicPay.api.responses.PageResponse;
import com.carbonaro.ReactiveSimplifiedPicPay.api.responses.transaction.TransactionResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

@Tag(name = "Transaction API - The NaturalPerson and LegalPerson transactions handler.")
@RequestMapping(value = "/transaction", produces = MediaType.APPLICATION_JSON_VALUE)
public interface ITransactionAPI {

    @GetMapping("/list-all")
    @PageableAsQueryParam
    @FindAllTransactionsRouteDescription
    @TransactionFilterRequestAsQueryParam
    Mono<PageResponse<TransactionResponse>> findAllTransactions(@Parameter(hidden = true) TransactionFilterRequest filterRequest);

    @PostMapping
    @TransactionRequestAsQueryParam
    @SaveTransactionRouteDescription
    Mono<Void> saveTransaction(@Parameter(hidden = true) TransactionRequest transaction);

}

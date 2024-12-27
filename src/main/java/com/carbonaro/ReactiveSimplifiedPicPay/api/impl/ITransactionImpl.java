package com.carbonaro.ReactiveSimplifiedPicPay.api.impl;

import static com.carbonaro.ReactiveSimplifiedPicPay.AppConstants.READ_ADMIN_SCOPE;
import static com.carbonaro.ReactiveSimplifiedPicPay.AppConstants.WRITE_USER_SCOPE;

import com.carbonaro.ReactiveSimplifiedPicPay.api.ITransactionAPI;
import com.carbonaro.ReactiveSimplifiedPicPay.api.requests.TransactionFilterRequest;
import com.carbonaro.ReactiveSimplifiedPicPay.api.requests.TransactionRequest;
import com.carbonaro.ReactiveSimplifiedPicPay.api.responses.PageResponse;
import com.carbonaro.ReactiveSimplifiedPicPay.api.responses.transaction.TransactionResponse;
import com.carbonaro.ReactiveSimplifiedPicPay.core.security.SecurityScopes;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.mappers.ITransactionMapper;
import com.carbonaro.ReactiveSimplifiedPicPay.services.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ITransactionImpl implements ITransactionAPI {

    private final TransactionService transactionService;
    private final ITransactionMapper transactionMapper;

    @Override
    @SecurityScopes(scopes = {READ_ADMIN_SCOPE})
    public Mono<PageResponse<TransactionResponse>> findAllTransactions(TransactionFilterRequest filterRequest) {

        var pageRequest = PageRequest.of(filterRequest.getPage(), filterRequest.getSize());
        return transactionService
                .findAllTransactions(pageRequest, filterRequest)
                .flatMap(transactionMapper::toPageTransactionResponse)
                .doOnSuccess(response -> log.info("List of all transactions was deployed with success!"));
    }

    @Override
    @SecurityScopes(scopes = {WRITE_USER_SCOPE})
    public Mono<Void> saveTransaction(TransactionRequest transaction) {

        return Mono
                .just(transaction)
                .map(transactionMapper::toTransactionByRequest)
                .flatMap(transactionService::saveTransaction)
                .doOnSuccess(transactionResponse ->
                        log.info("New Transaction was saved with success! New Transaction ID: {}", transactionResponse.getId()))
                .then();
    }

}

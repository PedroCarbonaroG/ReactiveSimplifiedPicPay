package com.carbonaro.ReactiveSimplifiedPicPay.api.impl;

import com.carbonaro.ReactiveSimplifiedPicPay.api.ITransactionAPI;
import com.carbonaro.ReactiveSimplifiedPicPay.core.security.SecuredDelegate;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.mappers.ITransactionMapper;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.requests.transaction.TransactionRequest;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.responses.transaction.TransactionResponse;
import com.carbonaro.ReactiveSimplifiedPicPay.services.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.carbonaro.ReactiveSimplifiedPicPay.AppConstants.ADMIN_WRITE_SCOPE;
import static com.carbonaro.ReactiveSimplifiedPicPay.AppConstants.USER_READ_SCOPE;

@RestController
@AllArgsConstructor
public class ITransactionImpl implements ITransactionAPI {

    private TransactionService transactionService;
    private ITransactionMapper transactionMapper;

    @Override
    @SecuredDelegate(scopes = USER_READ_SCOPE)
    public Flux<TransactionResponse> findAllTransactions() {

        return transactionService
                .findAllTransactions()
                .flatMap(transactionMapper::toTransactionResponse);
    }

    @Override
    @SecuredDelegate(scopes = ADMIN_WRITE_SCOPE)
    public Mono<Void> saveTransaction(TransactionRequest transaction) {

        return Mono
                .just(transaction)
                .map(transactionMapper::toTransactionByRequest)
                .flatMap(transactionService::saveTransaction);
    }

}

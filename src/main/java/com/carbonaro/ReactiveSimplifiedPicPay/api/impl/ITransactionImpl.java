package com.carbonaro.ReactiveSimplifiedPicPay.api.impl;

import com.carbonaro.ReactiveSimplifiedPicPay.api.ITransactionAPI;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.mappers.ITransactionMapper;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.requests.TransactionRequest;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.responses.transaction.TransactionResponse;
import com.carbonaro.ReactiveSimplifiedPicPay.services.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@AllArgsConstructor
public class ITransactionImpl implements ITransactionAPI {

    private TransactionService transactionService;
    private ITransactionMapper transactionMapper;

    @Override
    public Flux<TransactionResponse> findAllTransactions() {

        return transactionService
                .findAllTransactions()
                .flatMap(transactionMapper::toTransactionResponse);
    }

    @Override
    public Mono<Void> saveTransaction(TransactionRequest transaction) {

        return Mono
                .just(transaction)
                .map(transactionMapper::toTransactionByRequest)
                .flatMap(transactionService::saveTransaction);
    }

}

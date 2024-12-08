package com.carbonaro.ReactiveSimplifiedPicPay.api.impl;

import com.carbonaro.ReactiveSimplifiedPicPay.api.ITransactionAPI;
import com.carbonaro.ReactiveSimplifiedPicPay.api.requests.transaction.TransactionFilterRequest;
import com.carbonaro.ReactiveSimplifiedPicPay.api.responses.PageResponse;
import com.carbonaro.ReactiveSimplifiedPicPay.core.security.SecurityScopes;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.mappers.ITransactionMapper;
import com.carbonaro.ReactiveSimplifiedPicPay.api.requests.transaction.TransactionRequest;
import com.carbonaro.ReactiveSimplifiedPicPay.api.responses.transaction.TransactionResponse;
import com.carbonaro.ReactiveSimplifiedPicPay.services.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import static com.carbonaro.ReactiveSimplifiedPicPay.AppConstants.ADMIN_WRITE_SCOPE;
import static com.carbonaro.ReactiveSimplifiedPicPay.AppConstants.USER_READ_SCOPE;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ITransactionImpl implements ITransactionAPI {

    private final TransactionService transactionService;
    private final ITransactionMapper transactionMapper;

    @Override
    @SecurityScopes(scopes = USER_READ_SCOPE)
    public Mono<PageResponse<TransactionResponse>> findAllTransactions(TransactionFilterRequest filterRequest) {

        var pageRequest = PageRequest.of(filterRequest.getPage(), filterRequest.getSize());
        return transactionService
                .findAllTransactions(pageRequest, filterRequest)
                .flatMap(transactionMapper::toPageResponseTransactionResponse)
                .doOnSuccess(unused -> log.info("List of all transactions was deployed with success!"));
    }

    @Override
    @SecurityScopes(scopes = ADMIN_WRITE_SCOPE)
    public Mono<Void> saveTransaction(TransactionRequest transaction) {

        return Mono
                .just(transaction)
                .map(transactionMapper::toTransactionByRequest)
                .flatMap(transactionService::saveTransaction);
    }

}

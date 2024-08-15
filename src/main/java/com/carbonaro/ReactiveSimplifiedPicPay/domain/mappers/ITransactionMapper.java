package com.carbonaro.ReactiveSimplifiedPicPay.domain.mappers;

import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.Transaction;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.mappers.helpers.ITransactionMapperHelper;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.requests.TransactionRequest;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.responses.transaction.TransactionResponse;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

@Mapper(
        componentModel = "spring",
        uses = {ITransactionMapper.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class ITransactionMapper {

    @Autowired
    protected ITransactionMapperHelper transactionMapper;

    public abstract Transaction toTransactionByRequest(TransactionRequest transactionRequest);

    public Mono<TransactionResponse> toTransactionResponse(Transaction transaction) {

        TransactionResponse transactionResponse = new TransactionResponse();
        return Mono
                .just(transactionResponse)
                .flatMap(response -> transactionMapper.getSender(transaction.getSenderDocument()))
                .zipWith(transactionMapper.getReceiver(transaction.getReceiverDocument()))
                .map(tuple -> {
                    transactionResponse.setId(transaction.getId());
                    transactionResponse.setTransactionValue(transaction.getTransactionValue());
                    transactionResponse.setSender(tuple.getT1());
                    transactionResponse.setReceiver(tuple.getT2());
                    return transactionResponse;
                });
    }
}

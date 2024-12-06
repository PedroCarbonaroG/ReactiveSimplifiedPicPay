package com.carbonaro.ReactiveSimplifiedPicPay.domain.mappers;

import com.carbonaro.ReactiveSimplifiedPicPay.api.requests.TransactionRequest;
import com.carbonaro.ReactiveSimplifiedPicPay.api.responses.PageResponse;
import com.carbonaro.ReactiveSimplifiedPicPay.api.responses.person.PersonResponse;
import com.carbonaro.ReactiveSimplifiedPicPay.api.responses.transaction.TransactionResponse;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.Transaction;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.mappers.helpers.ITransactionMapperHelper;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@Mapper(
        componentModel = "spring",
        uses = {ITransactionMapper.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class ITransactionMapper {

    @Autowired
    protected ITransactionMapperHelper transactionMapper;

    public abstract Transaction toTransactionByRequest(TransactionRequest transactionRequest);

    public Mono<PageResponse<TransactionResponse>> toPageTransactionResponse(PageResponse<Transaction> pageOfTransactions) {

        return Flux
                .fromIterable(pageOfTransactions.getContent())
                .flatMap(this::buildTupleForTransactionResponse)
                .flatMap(this::setTransactionFields)
                .collectList()
                .map(transactionsResponse -> buildResponse(transactionsResponse, pageOfTransactions));
    }

    private PageResponse<TransactionResponse> buildResponse(List<TransactionResponse> transactions, PageResponse<Transaction> page) {

        return new PageResponse<>(
                page.getPageNumber(),
                page.getPageSize(),
                page.getTotalPages(),
                page.getTotalElements(),
                page.getNumberOfElements(),
                transactions);
    }

    private Mono<Tuple2<Tuple2<PersonResponse, PersonResponse>, Transaction>> buildTupleForTransactionResponse(Transaction transaction) {

        return Mono.just(transaction)
                .flatMap(self -> transactionMapper.getSenderToMapper(self)
                        .zipWith(transactionMapper.getReceiverToMapper(self))
                        .zipWith(Mono.just(self)));
    }

    private Mono<TransactionResponse> setTransactionFields(Tuple2<Tuple2<PersonResponse, PersonResponse>, Transaction> tuple) {

        return Mono.just(tuple)
                .flatMap(self -> {
                    TransactionResponse transactionResponse = new TransactionResponse();
                    transactionResponse.setId(self.getT2().getId());
                    transactionResponse.setTransactionValue(self.getT2().getTransactionValue());
                    transactionResponse.setSender(self.getT1().getT1());
                    transactionResponse.setReceiver(self.getT1().getT2());
                    transactionResponse.setTransactionDate(self.getT2().getTransactionDate());
                    return Mono.just(transactionResponse);
                });
    }

}

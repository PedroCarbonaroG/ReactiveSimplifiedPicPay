package com.carbonaro.ReactiveSimplifiedPicPay.repositories;

import com.carbonaro.ReactiveSimplifiedPicPay.api.requests.transaction.TransactionFilterRequest;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
public class TransactionRepository extends BaseRepository {

    private final ITransactionRepository repository;

    private static final String SENDER_DOCUMENT = "senderDocument";
    private static final String RECEIVER_DOCUMENT = "receiverDocument";
    private static final String TRANSACTION_VALUE = "transactionValue";
    private static final String TRANSACTION_DATE = "transactionDate";

    public Mono<Page<Transaction>> listAllPaged(Pageable pageRequest, TransactionFilterRequest filterRequest) {

        Query query = new Query();

        addParam(filterRequest.getSenderDocument(), SENDER_DOCUMENT, query);
        addParam(filterRequest.getReceiverDocument(), RECEIVER_DOCUMENT, query);
        addParamValues(filterRequest.getInitialValue(), filterRequest.getFinalValue(), TRANSACTION_VALUE, query);
        addParamBetweenDates(filterRequest.getInitialDate(), filterRequest.getFinalDate(), TRANSACTION_DATE, query);

        return toPage(query, pageRequest, Transaction.class);
    }

    public Mono<Transaction> save(Transaction transaction) {
        return repository.save(transaction);
    }

    public Mono<Void> deleteAll() {
        return repository.deleteAll();
    }

}

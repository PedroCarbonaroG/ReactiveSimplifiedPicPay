package com.carbonaro.ReactiveSimplifiedPicPay.repositories;

import com.carbonaro.ReactiveSimplifiedPicPay.api.requests.TransactionFilterRequest;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
@RequiredArgsConstructor
public class TransactionRepository extends BaseRepository {

    private final ITransactionRepository repository;

    private static final String SENDER_DOCUMENT_FIELD = "senderDocument";
    private static final String RECEIVER_DOCUMENT_FIELD = "receiverDocument";
    private static final String TRANSACTION_DATE_FIELD = "transactionDate";
    private static final String TRANSACTION_VALUE_FIELD = "transactionValue";

    public Mono<Page<Transaction>> findAll(Pageable page, TransactionFilterRequest filterRequest) {

        Query query = new Query();

        addParamToQuery(query, SENDER_DOCUMENT_FIELD, filterRequest.getSenderDocument());
        addParamToQuery(query, RECEIVER_DOCUMENT_FIELD, filterRequest.getReceiverDocument());
        addParamBetweenDates(query, TRANSACTION_DATE_FIELD, filterRequest.getInitialDate(), filterRequest.getFinalDate());
        addParamBetweenValues(query, TRANSACTION_VALUE_FIELD, filterRequest.getInitialTransactionValue(), filterRequest.getFinalTransactionValue());

        return toPage(query, page, Transaction.class);
    }

    public Mono<Transaction> save(Transaction transaction) {

        return repository
                .save(transaction);
    }

    public Mono<Void> deleteAll() {

        return repository
                .deleteAll();
    }

}

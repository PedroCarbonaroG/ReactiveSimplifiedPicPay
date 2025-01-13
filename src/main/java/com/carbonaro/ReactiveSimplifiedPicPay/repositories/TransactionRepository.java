package com.carbonaro.ReactiveSimplifiedPicPay.repositories;

import com.carbonaro.ReactiveSimplifiedPicPay.api.requests.transaction.TransactionFilterRequest;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.Person;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
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
    private static final String ID_FIELD = "id";
    private static final String BALANCE_FIELD = "balance";

    public Mono<Page<Transaction>> findAll(Pageable page, TransactionFilterRequest filterRequest) {

        Query query = new Query();

        addParamToQuery(query, SENDER_DOCUMENT_FIELD, filterRequest.getSenderDocument());
        addParamToQuery(query, RECEIVER_DOCUMENT_FIELD, filterRequest.getReceiverDocument());
        addParamBetweenDates(query, TRANSACTION_DATE_FIELD, filterRequest.getInitialDate(), filterRequest.getFinalDate());
        addParamBetweenValues(query, TRANSACTION_VALUE_FIELD, filterRequest.getInitialTransactionValue(), filterRequest.getFinalTransactionValue());

        return toPage(query, page, Transaction.class);
    }

    public Mono<Transaction> save(Transaction transaction) {
        return repository.save(transaction);
    }

    public Mono<Void> deleteAll() {
        return repository.deleteAll();
    }

    public Mono<Transaction> saveBalanceChange(Person sender, Person receiver, Transaction transaction) {

        return Mono.just(transaction)
                .map(unused -> {

                    Query senderQuery = new Query(Criteria.where(ID_FIELD).is(sender.getId()));
                    Query receiverQuery = new Query(Criteria.where(ID_FIELD).is(receiver.getId()));

                    Update senderUpdate = new Update();
                    Update receiverUpdate = new Update();

                    senderUpdate.set(BALANCE_FIELD, sender.getBalance().subtract(transaction.getTransactionValue()));
                    receiverUpdate.set(BALANCE_FIELD, receiver.getBalance().add(transaction.getTransactionValue()));

                    template.updateFirst(senderQuery, senderUpdate, sender.getClass()).subscribe();
                    template.updateFirst(receiverQuery, receiverUpdate, sender.getClass()).subscribe();

                    return transaction;
                });
    }

}

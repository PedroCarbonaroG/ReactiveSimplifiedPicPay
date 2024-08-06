package com.carbonaro.ReactiveSimplifiedPicPay.services;

import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.Transaction;
import com.carbonaro.ReactiveSimplifiedPicPay.repositories.TransactionRepository;
import com.carbonaro.ReactiveSimplifiedPicPay.services.exceptions.EmptyException;
import com.carbonaro.ReactiveSimplifiedPicPay.services.exceptions.EmptyOrNullObjectException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@AllArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final NaturalPersonService naturalPersonService;
    private final LegalPersonService legalPersonService;

    public Flux<Transaction> findAllTransactions() {

        return transactionRepository
                .findAll()
                .switchIfEmpty(Flux.error(new EmptyException()))
                .doOnComplete(() -> log.info("List of all transactions was deployed with success!"))
                .doOnError(errorResponse -> Flux.error(new Exception(errorResponse.getMessage())));
    }

    public Mono<Void> saveTransaction(Transaction transaction) {

        return Mono
                .just(transaction)
                .switchIfEmpty(Mono.error(new EmptyOrNullObjectException()))
                .flatMap(transactionRepository::save)
                .doOnSuccess(transactionResponse -> log.info("New Transaction was saved with success! New Transaction ID: {}", transactionResponse.getId()))
                .doOnError(errorResponse -> Mono.error(new Exception(errorResponse.getMessage())))
                .then();
    }
}

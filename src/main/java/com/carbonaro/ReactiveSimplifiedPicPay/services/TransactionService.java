package com.carbonaro.ReactiveSimplifiedPicPay.services;

import com.carbonaro.ReactiveSimplifiedPicPay.api.requests.transaction.TransactionFilterRequest;
import com.carbonaro.ReactiveSimplifiedPicPay.api.responses.PageResponse;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.Person;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.Transaction;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.mappers.ITransactionMapper;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.mappers.helpers.ITransactionMapperHelper;
import com.carbonaro.ReactiveSimplifiedPicPay.repositories.TransactionRepository;
import com.carbonaro.ReactiveSimplifiedPicPay.services.exceptions.BadRequestException;
import com.carbonaro.ReactiveSimplifiedPicPay.services.exceptions.EmptyException;
import com.carbonaro.ReactiveSimplifiedPicPay.services.exceptions.TransactionValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

import static com.carbonaro.ReactiveSimplifiedPicPay.AppConstants.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final ITransactionMapper transactionMapper;
    private final ITransactionMapperHelper transactionMapperHelper;
    private final TransactionRepository transactionRepository;

    public Mono<PageResponse<Transaction>> findAllTransactions(Pageable page, TransactionFilterRequest filterRequest) {

        return checkSenderDocument(filterRequest.getSenderDocument())
                .flatMap(self -> transactionRepository.findAll(page, filterRequest))
                .switchIfEmpty(Mono.error(new EmptyException(GENERAL_EMPTY_WARNING)))
                .map(transactionMapper::toPageResponseTransactionResponse);
    }
    private Mono<Void> checkSenderDocument(String document) {

        if (Objects.nonNull(document)) {
            if (document.length() != 11 || !document.matches(ONLY_NUMBERS)) {
                return Mono.error(new BadRequestException(TRANSACTION_INVALID_FILTER_SENDER_DOCUMENT));
            }
        }
        return Mono.empty();
    }

    public Mono<Transaction> saveTransaction(Transaction transaction) {

        transaction.setTransactionDate(LocalDateTime.now());
        return Mono
                .just(transaction)
                .flatMap(this::validateTransactionDocuments)
                .flatMap(this::validateTransactionValue)
                .flatMap(this::setTransactionDate)
                .flatMap(transactionRepository::save)
                .flatMap(this::getTransactionPersons)
                .flatMap(tuple -> transactionRepository.saveBalanceChange(tuple.getT1(), tuple.getT2(), transaction))
                .doOnError(errorResponse -> Mono.error(new Exception(errorResponse.getMessage())));
    }
    private Mono<Transaction> validateTransactionDocuments(Transaction transaction) {

        return Mono.fromCallable(() -> {
            if (!isValidDocument(transaction.getSenderDocument())) {
                throw new BadRequestException(TRANSACTION_INVALID_SENDER_DOCUMENT);
            }
            if (!isValidDocument(transaction.getReceiverDocument())) {
                throw new BadRequestException(TRANSACTION_INVALID_RECEIVER_DOCUMENT);
            }
            return transaction;
        });
    }
    private boolean isValidDocument(String document) {
        return (document.length() == 11 || document.length() == 14) && document.matches(ONLY_NUMBERS);
    }
    private Mono<Transaction> validateTransactionValue(Transaction transaction) {

        return Mono
                .just(transaction)
                .flatMap(transactionMapperHelper::getSenderForTransaction)
                .flatMap(self -> {

                    if (transaction.getTransactionValue().compareTo(BigDecimal.ZERO) < 0) {
                        return Mono.error(new TransactionValidationException(TRANSACTION_NEGATIVE_VALUE));
                    }

                    if (self.getBalance().compareTo(transaction.getTransactionValue()) < 0) {
                        return Mono.error(new TransactionValidationException(TRANSACTION_INSUFFICIENT_BALANCE));
                    }

                    return Mono.just(transaction);
                });
    }
    private Mono<Transaction> setTransactionDate(Transaction transaction) {

        return Mono.just(transaction)
                .map(self -> {
                    self.setTransactionDate(LocalDateTime.now());
                    return self;
                });
    }
    private Mono<Tuple2<Person, Person>> getTransactionPersons(Transaction transaction) {
        return Mono
                .zip(transactionMapperHelper.getSenderForTransaction(transaction),
                        transactionMapperHelper.getReceiverForTransaction(transaction));
    }

}

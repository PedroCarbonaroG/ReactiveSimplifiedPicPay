package com.carbonaro.ReactiveSimplifiedPicPay.services;

import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.LegalPerson;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.NaturalPerson;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.Transaction;
import com.carbonaro.ReactiveSimplifiedPicPay.repositories.TransactionRepository;
import com.carbonaro.ReactiveSimplifiedPicPay.services.exceptions.EmptyException;
import com.carbonaro.ReactiveSimplifiedPicPay.services.exceptions.TransactionValidationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.math.BigDecimal;
import java.util.List;

import static com.carbonaro.ReactiveSimplifiedPicPay.AppConstants.*;

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
                .flatMap(this::validateTransactionDocuments)
                .flatMap(this::validateTransactionValue)
                .flatMap(transactionRepository::save)
                .doOnSuccess(transactionResponse -> log.info("New Transaction was saved with success! New Transaction ID: {}", transactionResponse.getId()))
                .doOnError(errorResponse -> Mono.error(new Exception(errorResponse.getMessage())))
                .then();
    }
    private Mono<Transaction> validateTransactionDocuments(Transaction transaction) {

        return Mono
                .zip(naturalPersonService.findAllNaturals().collectList(), legalPersonService.findAllLegals().collectList())
                .flatMap(tuple -> {

                    var listOfNaturals = tuple.getT1();
                    var listOfLegals = tuple.getT2();

                    if (isLegalPerson(listOfLegals, transaction.getSenderDocument())) {
                        return Mono.error(new TransactionValidationException(LEGAL_SENDER_ERROR));
                    }

                    if (!isNaturalPerson(listOfNaturals, transaction.getSenderDocument())) {
                        return Mono.error(new TransactionValidationException(NATURAL_SENDER_ERROR));
                    }

                    if (!isValidReceiver(listOfNaturals, listOfLegals, transaction.getReceiverDocument())) {
                        return Mono.error(new TransactionValidationException(RECEIVER_ERROR));
                    }

                    return Mono.just(transaction);
                });
    }
    private boolean isLegalPerson(List<LegalPerson> listOfLegals, String document) {

        return listOfLegals.stream().anyMatch(self -> self.getCnpj().equals(document));
    }
    private boolean isNaturalPerson(List<NaturalPerson> listOfNaturals, String document) {

        return listOfNaturals.stream().anyMatch(self -> self.getCpf().equals(document));
    }
    private boolean isValidReceiver(List<NaturalPerson> listOfNaturals, List<LegalPerson> listOfLegals, String document) {

        return isNaturalPerson(listOfNaturals, document) || isLegalPerson(listOfLegals, document);
    }
    private Mono<Transaction> validateTransactionValue(Transaction transaction) {

        return Mono
                .zip(Mono.just(transaction), naturalPersonService.findNaturalByCPF(transaction.getSenderDocument()))
                .flatMap(tuple -> {

                    var senderBalance = tuple.getT2().getBalance();
                    return transaction.getTransactionValue().compareTo(BigDecimal.valueOf(1)) > 0 && transaction.getTransactionValue().compareTo(senderBalance) > 0
                            ? Mono.just(transaction)
                            : Mono.error(new TransactionValidationException(TRANSACTION_VALUES_ERROR));
                });
    }

}

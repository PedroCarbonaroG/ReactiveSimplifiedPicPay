package com.carbonaro.ReactiveSimplifiedPicPay.services;

import static com.carbonaro.ReactiveSimplifiedPicPay.AppConstants.GENERAL_EMPTY_WARNING;
import static com.carbonaro.ReactiveSimplifiedPicPay.AppConstants.TRANSACTION_INSUFFICIENT_BALANCE;
import static com.carbonaro.ReactiveSimplifiedPicPay.AppConstants.TRANSACTION_NEGATIVE_VALUE;

import com.carbonaro.ReactiveSimplifiedPicPay.api.requests.TransactionFilterRequest;
import com.carbonaro.ReactiveSimplifiedPicPay.api.responses.PageResponse;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.LegalPerson;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.NaturalPerson;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.Person;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.Transaction;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.mappers.PageMapper;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.mappers.helpers.ITransactionMapperHelper;
import com.carbonaro.ReactiveSimplifiedPicPay.repositories.TransactionRepository;
import com.carbonaro.ReactiveSimplifiedPicPay.services.exceptions.EmptyException;
import com.carbonaro.ReactiveSimplifiedPicPay.services.exceptions.TransactionValidationException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final NaturalPersonService naturalPersonService;
    private final LegalPersonService legalPersonService;
    private final ITransactionMapperHelper transactionMapperHelper;
    private final ReactiveMongoTemplate mongoTemplate;
    private final PageMapper<Transaction> pageMapper;

    public Mono<PageResponse<Transaction>> findAllTransactions(Pageable page, TransactionFilterRequest filterRequest) {

        return transactionRepository
                .findAll(page, filterRequest)
                .map(pageMapper::toPageResponse)
                .switchIfEmpty(Mono.error(new EmptyException(GENERAL_EMPTY_WARNING)))
                .doOnError(errorResponse -> Flux.error(new Exception(errorResponse.getMessage())));
    }

    public Mono<Transaction> saveTransaction(Transaction transaction) {

        transaction.setTransactionDate(LocalDateTime.now());
        return Mono
                .just(transaction)
                .flatMap(this::validateTransactionDocuments)
                .flatMap(this::validateTransactionValue)
                .flatMap(this::setTransactionDate)
                .flatMap(transactionRepository::save)
                .flatMap(this::saveBalanceChange)
                .doOnError(errorResponse -> Mono.error(new Exception(errorResponse.getMessage())));
    }
    private Mono<Transaction> validateTransactionDocuments(Transaction transaction) {

        return Mono.just(transaction);
//        return Mono
//                .zip(naturalPersonService.findAllNaturals().collectList(),
//                        legalPersonService.findAllLegals().collectList())
//                .flatMap(tuple -> {
//
//                    var listOfNaturals = tuple.getT1();
//                    var listOfLegals = tuple.getT2();
//
//                    if (isLegalPerson(listOfLegals, transaction.getSenderDocument())) {
//                        return Mono.error(new TransactionValidationException(TRANSACTION_LEGAL_SENDER_ERROR));
//                    }
//
//                    if (!isNaturalPerson(listOfNaturals, transaction.getSenderDocument())) {
//                        return Mono.error(new TransactionValidationException(TRANSACTION_NATURAL_SENDER_ERROR));
//                    }
//
//                    if (!isValidReceiver(listOfNaturals, listOfLegals, transaction.getReceiverDocument())) {
//                        return Mono.error(new TransactionValidationException(TRANSACTION_RECEIVER_ERROR));
//                    }
//
//                    return Mono.just(transaction);
//                });
    }
    private boolean isLegalPerson(List<LegalPerson> listOfLegals, String document) {

        return listOfLegals
                .stream()
                .anyMatch(self -> self.getCnpj().equals(document));
    }
    private boolean isNaturalPerson(List<NaturalPerson> listOfNaturals, String document) {

        return listOfNaturals
                .stream()
                .anyMatch(self -> self.getCpf().equals(document));
    }
    private boolean isValidReceiver(List<NaturalPerson> listOfNaturals, List<LegalPerson> listOfLegals, String document) {

        return isNaturalPerson(listOfNaturals, document) || isLegalPerson(listOfLegals, document);
    }
    private Mono<Transaction> validateTransactionValue(Transaction transaction) {

        return Mono
                .just(transaction)
                .flatMap(transactionMapperHelper::getSenderForTransaction)
                .flatMap(self -> {

                    if (transaction.getTransactionValue().compareTo(BigDecimal.ZERO) < 0) {
                        return Mono.error(new TransactionValidationException(TRANSACTION_NEGATIVE_VALUE));
                    }

                    if (validateSenderBalance(self, transaction)) {
                        return Mono.error(new TransactionValidationException(TRANSACTION_INSUFFICIENT_BALANCE));
                    }

                    return Mono.just(transaction);
                });
    }
    private boolean validateSenderBalance(Person sender, Transaction transaction) {

        return sender instanceof NaturalPerson && ((NaturalPerson) sender).getBalance().compareTo(transaction.getTransactionValue()) < 0 ||
                sender instanceof LegalPerson && ((LegalPerson) sender).getBalance().compareTo(transaction.getTransactionValue()) < 0;
    }
    private Mono<Transaction> setTransactionDate(Transaction transaction) {

        return Mono.just(transaction)
                .map(self -> {
                    self.setTransactionDate(LocalDateTime.now());
                    return self;
                });
    }
    private Mono<Transaction> saveBalanceChange(Transaction transaction) {

        return Mono
                .zip(transactionMapperHelper.getSenderForTransaction(transaction),
                        transactionMapperHelper.getReceiverForTransaction(transaction))
                .flatMap(tuple -> {

                    var sender = tuple.getT1();
                    var receiver = tuple.getT2();

                    Query senderQuery = new Query(Criteria.where("id").is(sender.getId()));
                    Query receiverQuery = new Query(Criteria.where("id").is(receiver.getId()));

                    Update senderUpdate = new Update();
                    Update receiverUpdate = new Update();

                    var newSenderBalance = setNewSenderBalance(sender, transaction);
                    var newReceiverBalance = setNewReceiverBalance(receiver, transaction);

                    senderUpdate.set("balance", newSenderBalance);
                    receiverUpdate.set("balance", newReceiverBalance);

                    mongoTemplate.updateFirst(senderQuery, senderUpdate, sender.getClass()).subscribe();
                    mongoTemplate.updateFirst(receiverQuery, receiverUpdate, sender.getClass()).subscribe();

                    return Mono.just(transaction);
                });
    }
    private BigDecimal setNewSenderBalance(Person sender, Transaction transaction) {

        return sender instanceof NaturalPerson
                ? ((NaturalPerson) sender).getBalance().subtract(transaction.getTransactionValue())
                : ((LegalPerson) sender).getBalance().subtract(transaction.getTransactionValue());
    }
    private BigDecimal setNewReceiverBalance(Person receiver, Transaction transaction) {

        return receiver instanceof NaturalPerson
                ? ((NaturalPerson) receiver).getBalance().add(transaction.getTransactionValue())
                : ((LegalPerson) receiver).getBalance().add(transaction.getTransactionValue());
    }

}

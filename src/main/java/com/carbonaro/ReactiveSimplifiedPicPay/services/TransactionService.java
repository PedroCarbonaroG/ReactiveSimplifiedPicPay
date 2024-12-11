package com.carbonaro.ReactiveSimplifiedPicPay.services;

import com.carbonaro.ReactiveSimplifiedPicPay.api.requests.transaction.TransactionFilterRequest;
import com.carbonaro.ReactiveSimplifiedPicPay.api.responses.PageResponse;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.LegalPerson;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.NaturalPerson;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.Person;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.Transaction;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.mappers.PageMapper;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.mappers.helpers.ITransactionMapperHelper;
import com.carbonaro.ReactiveSimplifiedPicPay.repositories.TransactionRepository;
import com.carbonaro.ReactiveSimplifiedPicPay.services.exceptions.EmptyReturnException;
import com.carbonaro.ReactiveSimplifiedPicPay.services.exceptions.TransactionValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.math.BigDecimal;
import java.util.List;

import static com.carbonaro.ReactiveSimplifiedPicPay.AppConstants.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final PageMapper<Transaction> pageMapper;
    private final TransactionRepository transactionRepository;
    private final NaturalPersonService naturalPersonService;
    private final LegalPersonService legalPersonService;
    private final ITransactionMapperHelper transactionMapperHelper;

    public Mono<PageResponse<Transaction>> findAllTransactions(Pageable page, TransactionFilterRequest filterRequest) {

        return transactionRepository
                .listAllPaged(page, filterRequest)
                .switchIfEmpty(Mono.error(new EmptyReturnException(GENERAL_WARNING_EMPTY)))
                .map(pageMapper::toPageResponse)
                .doOnError(errorResponse -> Mono.error(new Exception(errorResponse.getMessage())));
    }

    public Mono<Void> saveTransaction(Transaction transaction) {

        return Mono
                .just(transaction)
                .flatMap(this::validateTransactionValue)
                .flatMap(transactionRepository::save)
                .doOnSuccess(transactionResponse -> log.info("New Transaction was saved with success! New Transaction ID: {}", transactionResponse.getId()))
                .doOnError(errorResponse -> Mono.error(new Exception(errorResponse.getMessage())))
                .then();
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
    private BigDecimal setNewSenderBalance(Person sender, Transaction transaction) {

        return sender instanceof NaturalPerson
                ? ((NaturalPerson) sender).getBalance().subtract(transaction.getTransactionValue())
                : ((LegalPerson) sender).getBalance().subtract(transaction.getTransactionValue());
    }
    private BigDecimal setNewReceiverBalance(Person receiver, Transaction transaction) {

        return receiver instanceof NaturalPerson
                ? ((NaturalPerson) receiver).getBalance().subtract(transaction.getTransactionValue())
                : ((LegalPerson) receiver).getBalance().subtract(transaction.getTransactionValue());
    }

}

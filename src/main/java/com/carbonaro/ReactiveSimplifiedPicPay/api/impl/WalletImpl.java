package com.carbonaro.ReactiveSimplifiedPicPay.api.impl;

import com.carbonaro.ReactiveSimplifiedPicPay.api.IWalletAPI;
import com.carbonaro.ReactiveSimplifiedPicPay.services.WalletService;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
public class WalletImpl implements IWalletAPI {

    private final WalletService walletService;

    @Override
    public Mono<Void> deposit(String document, BigDecimal amount) {

        return walletService
                .deposit(document, amount)
                .doOnSuccess(unused -> log.info("Deposit of {} to account {} was successful.", amount, document));
    }

    @Override
    public Mono<BigDecimal> consultBalance(String document) {

        log.info("Consulting balance for account: {}", document);
        return walletService
                .consultBalance(document)
                .doOnSuccess(unused -> log.info("Balance for account: {}, consulted successfully.", document));
    }

}

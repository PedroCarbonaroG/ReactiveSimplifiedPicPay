package com.carbonaro.ReactiveSimplifiedPicPay.api;

import com.carbonaro.ReactiveSimplifiedPicPay.api.annotations.route_description.ConsultBalanceRouteDescription;
import com.carbonaro.ReactiveSimplifiedPicPay.api.annotations.route_description.DepositRouteDescription;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.math.BigDecimal;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@Tag(name = "Wallet API - Monetary environment")
@RequestMapping(value = "/wallet", produces = MediaType.APPLICATION_JSON_VALUE)
public interface IWalletAPI {

    @PostMapping("/deposit")
    @DepositRouteDescription
    Mono<Void> deposit(@RequestHeader String document,
                       @Parameter(required = true) BigDecimal amount);

    @GetMapping("/balance")
    @ConsultBalanceRouteDescription
    Mono<BigDecimal> consultBalance(@RequestHeader String document);

}

package com.carbonaro.ReactiveSimplifiedPicPay.domain.requests.transaction;

import jdk.jfr.Description;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {

    @Description("Identifier from the transaction sender.")
    private String senderDocument;

    @Description("Identifier from the transaction receiver.")
    private String receiverDocument;

    @Description("Total transaction amount.")
    private BigDecimal transactionValue;

}

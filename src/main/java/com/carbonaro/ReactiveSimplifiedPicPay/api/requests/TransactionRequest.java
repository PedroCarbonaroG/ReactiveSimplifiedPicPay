package com.carbonaro.ReactiveSimplifiedPicPay.api.requests;

import java.math.BigDecimal;
import jdk.jfr.Description;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
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

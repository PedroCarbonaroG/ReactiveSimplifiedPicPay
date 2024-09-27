package com.carbonaro.ReactiveSimplifiedPicPay.domain.requests.transaction;

import io.swagger.v3.oas.annotations.media.Schema;
import jdk.jfr.Description;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {

    @Schema(description = "Identifier from the transaction sender.")
    private String senderDocument;

    @Schema(description = "Identifier from the transaction receiver.")
    private String receiverDocument;

    @Schema(description = "Total transaction amount.")
    private BigDecimal transactionValue;

}

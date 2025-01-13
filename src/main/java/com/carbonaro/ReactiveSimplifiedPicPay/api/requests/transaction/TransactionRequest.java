package com.carbonaro.ReactiveSimplifiedPicPay.api.requests.transaction;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
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

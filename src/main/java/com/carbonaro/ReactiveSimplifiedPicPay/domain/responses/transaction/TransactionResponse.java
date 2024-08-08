package com.carbonaro.ReactiveSimplifiedPicPay.domain.responses.transaction;

import com.carbonaro.ReactiveSimplifiedPicPay.domain.responses.person.PersonResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {

    @Schema(description = "Transaction Identifier.")
    private String id;

    @Schema(description = "Identifier from the transaction sender.")
    private PersonResponse sender;

    @Schema(description = "Identifier from the transaction receiver.")
    private PersonResponse receiver;

    @Schema(description = "Total transaction amount.")
    private BigDecimal transactionValue;
}

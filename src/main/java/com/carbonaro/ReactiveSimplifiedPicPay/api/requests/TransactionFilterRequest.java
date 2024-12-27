package com.carbonaro.ReactiveSimplifiedPicPay.api.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TransactionFilterRequest extends PagedRequest {

    @Schema(description = "Filter for sender document.")
    private String senderDocument;

    @Schema(description = "Filter for receiver document.")
    private String receiverDocument;

    @Schema(description = "Filter for transaction initial date.")
    private LocalDate initialDate;

    @Schema(description = "Filter for transaction final date.")
    private LocalDate finalDate;

    @Schema(description = "Filter for transaction initial value.")
    private BigDecimal initialTransactionValue;

    @Schema(description = "Filter for transaction final value.")
    private BigDecimal finalTransactionValue;

}

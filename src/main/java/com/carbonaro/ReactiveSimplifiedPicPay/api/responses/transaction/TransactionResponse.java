package com.carbonaro.ReactiveSimplifiedPicPay.api.responses.transaction;

import com.carbonaro.ReactiveSimplifiedPicPay.api.responses.person.PersonResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
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

    @Schema(description = "Transaction date.")
    @JsonFormat(pattern = "dd/MM/yyyy - HH:mm:ss")
    @DateTimeFormat(pattern = "dd/MM/yyyy - HH:mm:ss")
    private LocalDateTime transactionDate;

}

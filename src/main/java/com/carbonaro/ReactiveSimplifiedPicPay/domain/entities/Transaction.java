package com.carbonaro.ReactiveSimplifiedPicPay.domain.entities;

import jdk.jfr.Description;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(value = "Transaction")
public class Transaction {

    @Id
    @Description("Transaction Identifier.")
    private String id;

    @Description("Identifier from the transaction sender.")
    private String senderDocument;

    @Description("Identifier from the transaction receiver.")
    private String receiverDocument;

    @Description("Total transaction amount.")
    private BigDecimal transactionValue;

    @Description("Transaction effective date")
    private LocalDate transactionDate;

}

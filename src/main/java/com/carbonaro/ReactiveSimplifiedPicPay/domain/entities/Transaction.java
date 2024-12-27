package com.carbonaro.ReactiveSimplifiedPicPay.domain.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import jdk.jfr.Description;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Transaction")
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

    @Description("Transaction date.")
    @JsonFormat(pattern = "dd/MM/yyyy - HH:mm:ss")
    @DateTimeFormat(pattern = "dd/MM/yyyy - HH:mm:ss")
    private LocalDateTime transactionDate;

}

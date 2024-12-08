package com.carbonaro.ReactiveSimplifiedPicPay.api.requests.transaction;

import com.carbonaro.ReactiveSimplifiedPicPay.api.requests.PagedRequest;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TransactionFilterRequest extends PagedRequest {

    private String senderDocument;
    private String receiverDocument;
    private LocalDate initialDate;
    private LocalDate finalDate;
    private BigDecimal initialValue;
    private BigDecimal finalValue;

}

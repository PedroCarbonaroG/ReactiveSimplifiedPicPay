package com.carbonaro.ReactiveSimplifiedPicPay;

import lombok.experimental.UtilityClass;
import org.springframework.context.annotation.PropertySource;

@UtilityClass
@PropertySource("classpath:src/main/resources/messages.properties")
public class AppConstants {

    public static final String LEGAL_SENDER_ERROR = "Transaction.err.legal.sender";
    public static final String NATURAL_SENDER_ERROR = "Transaction.err.natural.sender";
    public static final String RECEIVER_ERROR = "Transaction.err.receiver";
    public static final String TRANSACTION_VALUES_ERROR = "Transaction.err.transaction.values";

}

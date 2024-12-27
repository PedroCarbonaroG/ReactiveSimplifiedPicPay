package com.carbonaro.ReactiveSimplifiedPicPay;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AppConstants {

    /* REFERENCES TO EXCEPTION MESSAGES */
    public static final String TRANSACTION_NEGATIVE_VALUE = "transaction.err.negative.value";
    public static final String TRANSACTION_INSUFFICIENT_BALANCE = "transaction.err.insufficient.balance";
    public static final String TRANSACTION_LEGAL_SENDER_ERROR = "transaction.err.legal.sender";
    public static final String TRANSACTION_NATURAL_SENDER_ERROR = "transaction.err.natural.sender";
    public static final String TRANSACTION_RECEIVER_ERROR = "transaction.err.receiver";

    public static final String GENERAL_INVALID_DOCUMENT_FORMAT = "invalid.document.format";
    public static final String GENERAL_EMPTY_WARNING = "general.warning.empty";
    public static final String DEFAULT_MESSAGE = "default.message";

    /* SCOPES FOR ROUTES */
    public static final String READ_USER_SCOPE = "user:read";
    public static final String WRITE_USER_SCOPE = "user:write";
    public static final String READ_ADMIN_SCOPE = "admin:read";
    public static final String WRITE_ADMIN_SCOPE = "admin:write";

}

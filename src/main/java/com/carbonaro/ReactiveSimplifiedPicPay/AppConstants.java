package com.carbonaro.ReactiveSimplifiedPicPay;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AppConstants {

    //# Global system messages
    public static final String GENERAL_WARNING_EMPTY = "general.warn.empty";
    public static final String GENERAL_UNKNOWN_EXCEPTION_ERROR = "general.err.unknown.exception";

    public static final String NATURAL_SAVE_ALREADY_EXISTS = "natural.err.save.already.exist";
    public static final String NATURAL_SAVE_INCORRECT_FIELD = "natural.err.save.incorrect.field";

    public static final String TRANSACTION_NEGATIVE_VALUE = "transaction.err.negative.value";
    public static final String TRANSACTION_INSUFFICIENT_BALANCE = "transaction.err.insufficient.balance";
    public static final String TRANSACTION_LEGAL_SENDER_ERROR = "transaction.err.legal.sender";
    public static final String TRANSACTION_NATURAL_SENDER_ERROR = "transaction.err.natural.sender";
    public static final String TRANSACTION_RECEIVER_ERROR = "transaction.err.receiver";

    public static final String TOKEN_EXPIRED_ERROR = "token.err.expired";
    public static final String TOKEN_SIGNATURE_ERROR = "token.err.signature";
    public static final String TOKEN_STRUCTURE_ERROR = "token.err.structure";
    public static final String TOKEN_SCOPES_ERROR = "token.err.scopes";

    //# Scopes for routes permissions
    public static final String USER_READ_SCOPE = "user:read";
    public static final String USER_WRITE_SCOPE = "user:write";
    public static final String ADMIN_READ_SCOPE = "admin:read";
    public static final String ADMIN_WRITE_SCOPE = "admin:write";
}

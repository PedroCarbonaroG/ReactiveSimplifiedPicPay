package com.carbonaro.ReactiveSimplifiedPicPay;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AppConstants {

    /* REFERENCES TO EXCEPTION MESSAGES */
    public static final String TRANSACTION_NEGATIVE_VALUE = "transaction.err.negative.value";
    public static final String TRANSACTION_INSUFFICIENT_BALANCE = "transaction.err.insufficient.balance";

    public static final String GENERAL_EMPTY_WARNING = "general.warning.empty";
    public static final String GENERAL_MESSAGE_HELPER_DEFAULT_MESSAGE = "general.message.helper.default.message";

    public static final String HANDLER_NOT_FOUND_ERROR_MESSAGE = "handler.not.found.error.message";
    public static final String HANDLER_BAD_REQUEST_ERROR_MESSAGE = "handler.bad.request.error.message";
    public static final String HANDLER_NO_CONTENT_WARNING_MESSAGE = "handler.no.content.warning.message";
    public static final String HANDLER_INTERNAL_SERVER_ERROR_MESSAGE =  "handler.internal.server.error.message";
    public static final String HANDLER_ILLEGAL_ARGUMENT_ERROR_MESSAGE = "handler.illegal.argument.error.message";
    public static final String HANDLER_MALFORMED_JWT_ERROR_MESSAGE = "handler.malformed.jwt.error.message";
    public static final String HANDLER_EXPIRED_JWT_ERROR_MESSAGE = "handler.expired.jwt.error.message";
    public static final String HANDLER_SIGNATURE_ERROR_MESSAGE = "handler.signature.error.message";

    /* SCOPES FOR ROUTES */
    public static final String READ_USER_SCOPE = "user:read";
    public static final String WRITE_USER_SCOPE = "user:write";
    public static final String READ_ADMIN_SCOPE = "admin:read";

}

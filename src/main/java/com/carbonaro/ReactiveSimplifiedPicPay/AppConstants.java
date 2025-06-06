package com.carbonaro.ReactiveSimplifiedPicPay;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AppConstants {

    /* GLOBAL CONSTANTS */
    public static final String ONLY_NUMBERS = "^\\d+$";

    /* REFERENCES TO EXCEPTION MESSAGES */
    public static final String OAUTH_USER_NOT_FOUND = "oauth.user.err.not.found";
    public static final String WALLET_NEGATIVE_AMOUNT_FOR_DEPOSIT = "wallet.err.negative.amount.for.deposit";
    public static final String WALLET_INVALID_DOCUMENT_FORMAT = "wallet.err.invalid.document.format";
    public static final String TRANSACTION_NEGATIVE_VALUE = "transaction.err.negative.value";
    public static final String TRANSACTION_INSUFFICIENT_BALANCE = "transaction.err.insufficient.balance";
    public static final String TRANSACTION_INVALID_SENDER_DOCUMENT = "transaction.err.invalid.sender.document";
    public static final String TRANSACTION_INVALID_FILTER_SENDER_DOCUMENT = "transaction.err.invalid.filter.sender.document";
    public static final String TRANSACTION_INVALID_RECEIVER_DOCUMENT = "transaction.err.invalid.receiver.document";

    public static final String GENERAL_EMPTY_WARNING = "general.warning.empty";
    public static final String GENERAL_MESSAGE_HELPER_DEFAULT_MESSAGE = "general.message.helper.default.message";

    public static final String HANDLER_NOT_FOUND_ERROR_MESSAGE = "handler.not.found.error.message";
    public static final String HANDLER_UNAUTHORIZED_ACCESS_DENIED_ERROR_MESSAGE = "handler.unauthorized.access.denied.error.message";
    public static final String HANDLER_BAD_REQUEST_ERROR_MESSAGE = "handler.bad.request.error.message";
    public static final String HANDLER_NO_CONTENT_WARNING_MESSAGE = "handler.no.content.warning.message";
    public static final String HANDLER_INTERNAL_SERVER_ERROR_MESSAGE =  "handler.internal.server.error.message";
    public static final String HANDLER_ILLEGAL_ARGUMENT_ERROR_MESSAGE = "handler.illegal.argument.error.message";
    public static final String HANDLER_MALFORMED_JWT_ERROR_MESSAGE = "handler.malformed.jwt.error.message";
    public static final String HANDLER_EXPIRED_JWT_ERROR_MESSAGE = "handler.expired.jwt.error.message";
    public static final String HANDLER_SIGNATURE_ERROR_MESSAGE = "handler.signature.error.message";
    public static final String HANDLER_AUTHENTICATION_EXCEPTION = "handler.authentication.error.message";
    public static final String HANDLER_AUTHENTICATION_SERVICE_EXCEPTION = "handler.authentication.service.error.message";

    /* SCOPES FOR ROUTES */
    public static final String USER_SCOPE = "hasRole('USER')";
    public static final String ADMIN_SCOPE = "hasRole('ADMIN')";

}

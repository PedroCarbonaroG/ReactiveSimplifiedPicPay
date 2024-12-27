package com.carbonaro.ReactiveSimplifiedPicPay.services.exceptions;

public class TransactionValidationException extends RuntimeException {

    private static final String DEFAULT_ERROR_MESSAGE = "Some field(s) in transaction are wrong.";

    public TransactionValidationException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

    public TransactionValidationException(String errorMessage) {
        super(errorMessage);
    }

}

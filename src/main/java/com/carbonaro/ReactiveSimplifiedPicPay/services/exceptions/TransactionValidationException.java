package com.carbonaro.ReactiveSimplifiedPicPay.services.exceptions;

public class TransactionValidationException extends RuntimeException {

    public TransactionValidationException(String errorMessage) {
        super(errorMessage);
    }

}

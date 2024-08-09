package com.carbonaro.ReactiveSimplifiedPicPay.services.exceptions;

public class EmptyOrNullObjectException extends RuntimeException {

    private static final String DEFAULT_ERROR_MESSAGE = "The Object was empty, missing fields or null!";

    public EmptyOrNullObjectException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

    public EmptyOrNullObjectException(String errorMsg) {
        super(errorMsg);
    }
}

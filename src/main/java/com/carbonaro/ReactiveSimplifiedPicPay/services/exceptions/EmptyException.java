package com.carbonaro.ReactiveSimplifiedPicPay.services.exceptions;

public class EmptyException extends RuntimeException {

    private static final String DEFAULT_ERROR_MESSAGE = "The search was done with success but non Object(s) was found!";

    public EmptyException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

    public EmptyException(String errorMsg) {
        super(errorMsg);
    }
}

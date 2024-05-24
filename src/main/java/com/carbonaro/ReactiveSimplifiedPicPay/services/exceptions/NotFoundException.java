package com.carbonaro.ReactiveSimplifiedPicPay.services.exceptions;

public class NotFoundException extends RuntimeException {

    private static final String DEFAULT_ERROR_MESSAGE = "Object(s) was not found!";

    public NotFoundException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

    public NotFoundException(String errorMsg) {
        super(errorMsg);
    }
}

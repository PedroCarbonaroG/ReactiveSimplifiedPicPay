package com.carbonaro.ReactiveSimplifiedPicPay.services.exceptions;

public class BadRequestException extends RuntimeException {

    private static final String DEFAULT_ERROR_MESSAGE = "Some field that is needed is null or empty, try again!";

    public BadRequestException() {

        super(DEFAULT_ERROR_MESSAGE);
    }

    public BadRequestException(String errorMsg) {

        super(errorMsg);
    }
}

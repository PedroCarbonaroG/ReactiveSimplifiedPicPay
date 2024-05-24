package com.carbonaro.ReactiveSimplifiedPicPay.services.exceptions;

public class InternalServerErrorException extends RuntimeException {

    private static final String DEFAULT_ERROR_MESSAGE = "Internal Server Error! Contact the administration.";

    public InternalServerErrorException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

    public InternalServerErrorException(String errorMsg) {
        super(errorMsg);
    }
}

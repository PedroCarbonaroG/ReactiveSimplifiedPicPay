package com.carbonaro.ReactiveSimplifiedPicPay.services.exceptions;

public class BadRequestException extends RuntimeException {

    public BadRequestException(String errorMsg) {

        super(errorMsg);
    }

}

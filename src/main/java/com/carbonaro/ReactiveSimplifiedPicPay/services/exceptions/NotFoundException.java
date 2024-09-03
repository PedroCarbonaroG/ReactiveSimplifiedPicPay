package com.carbonaro.ReactiveSimplifiedPicPay.services.exceptions;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String errorMsg) {
        super(errorMsg);
    }

}

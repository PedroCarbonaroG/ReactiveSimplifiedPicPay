package com.carbonaro.ReactiveSimplifiedPicPay.services.exceptions;

public class EmptyOrNullObjectException extends RuntimeException {

    public EmptyOrNullObjectException(String errorMsg) {
        super(errorMsg);
    }

}

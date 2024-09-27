package com.carbonaro.ReactiveSimplifiedPicPay.services.exceptions;

public class EmptyReturnException extends RuntimeException {

    public EmptyReturnException(String errorMsg) {
        super(errorMsg);
    }

}

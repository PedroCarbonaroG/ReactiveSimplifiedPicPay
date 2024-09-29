package com.carbonaro.ReactiveSimplifiedPicPay.api.impl;

import com.carbonaro.ReactiveSimplifiedPicPay.api.IExportingAPI;
import com.carbonaro.ReactiveSimplifiedPicPay.core.security.SecuredDelegate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import static com.carbonaro.ReactiveSimplifiedPicPay.AppConstants.USER_READ_SCOPE;

@RestController
public class IExportingImpl implements IExportingAPI {

    @Override
    @SecuredDelegate(scopes = {USER_READ_SCOPE})
    public Mono<ResponseEntity<byte[]>> getPersonPdfExtraction() {

        return null;
    }

    @Override
    @SecuredDelegate(scopes = {USER_READ_SCOPE})
    public Mono<ResponseEntity<byte[]>> getPersonExcelExtraction() {

        return null;
    }

    @Override
    @SecuredDelegate(scopes = {USER_READ_SCOPE})
    public Mono<ResponseEntity<byte[]>> getTransactionPdfExtraction() {

        return null;
    }

    @Override
    @SecuredDelegate(scopes = {USER_READ_SCOPE})
    public Mono<ResponseEntity<byte[]>> getTransactionExcelExtraction() {

        return null;
    }

}

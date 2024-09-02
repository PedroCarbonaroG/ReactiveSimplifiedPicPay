package com.carbonaro.ReactiveSimplifiedPicPay.api.impl;

import com.carbonaro.ReactiveSimplifiedPicPay.api.ITokenAPI;
import com.carbonaro.ReactiveSimplifiedPicPay.services.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class ITokenImpl implements ITokenAPI {

    private final TokenService tokenService;

    @Override
    public String generateUserToken() {

        return tokenService.generateUserToken();
    }

    @Override
    public String generateAdminToken() {

        return tokenService.generateAdminToken();
    }
}
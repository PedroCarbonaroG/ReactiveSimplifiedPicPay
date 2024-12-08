package com.carbonaro.ReactiveSimplifiedPicPay.api.impl;

import com.carbonaro.ReactiveSimplifiedPicPay.api.ITokenAPI;
import com.carbonaro.ReactiveSimplifiedPicPay.services.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
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

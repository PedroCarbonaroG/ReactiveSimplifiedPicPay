package com.carbonaro.ReactiveSimplifiedPicPay.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Wallet API - Person wallet management")
@RequestMapping(value = "/transaction", produces = MediaType.APPLICATION_JSON_VALUE)
public interface IWalletAPI {



}

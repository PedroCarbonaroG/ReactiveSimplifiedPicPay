package com.carbonaro.ReactiveSimplifiedPicPay.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "ITokenAPI - Responsible API for generate authorization tokens")
@RequestMapping(value = "/person", produces = MediaType.APPLICATION_JSON_VALUE)
public interface ITokenAPI {

}

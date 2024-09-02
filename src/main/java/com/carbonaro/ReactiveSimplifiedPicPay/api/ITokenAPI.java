package com.carbonaro.ReactiveSimplifiedPicPay.api;

import com.carbonaro.ReactiveSimplifiedPicPay.core.annotations.RouteDescriptions.GenerateAdminTokenRouteDescription;
import com.carbonaro.ReactiveSimplifiedPicPay.core.annotations.RouteDescriptions.GenerateUserTokenRouteDescription;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "ITokenAPI - Responsible API for generate authorization tokens")
@RequestMapping(value = "/token")
public interface ITokenAPI {

    @PostMapping("/user")
    @GenerateUserTokenRouteDescription
    String generateUserToken();

    @PostMapping("/admin")
    @GenerateAdminTokenRouteDescription
    String generateAdminToken();

}

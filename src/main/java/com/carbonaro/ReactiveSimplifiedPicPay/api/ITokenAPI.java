package com.carbonaro.ReactiveSimplifiedPicPay.api;

import com.carbonaro.ReactiveSimplifiedPicPay.api.annotations.route_description.GenerateAdminTokenRouteDescription;
import com.carbonaro.ReactiveSimplifiedPicPay.api.annotations.route_description.GenerateUserTokenRouteDescription;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "ITokenAPI - Token generation")
@RequestMapping(value = "/token")
public interface ITokenAPI {

    @PostMapping("/user")
    @GenerateUserTokenRouteDescription
    String generateUserToken();

    @PostMapping("/admin")
    @GenerateAdminTokenRouteDescription
    String generateAdminToken();

}

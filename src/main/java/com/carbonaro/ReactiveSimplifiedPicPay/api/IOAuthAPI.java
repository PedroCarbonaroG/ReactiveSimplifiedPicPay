package com.carbonaro.ReactiveSimplifiedPicPay.api;

import com.carbonaro.ReactiveSimplifiedPicPay.api.annotations.route_description.GenerateAdminTokenRouteDescription;
import com.carbonaro.ReactiveSimplifiedPicPay.api.annotations.route_description.GenerateUserTokenRouteDescription;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@Tag(name = "OAuthAPI - User authentication & Token generation")
@RequestMapping(value = "/oauth")
public interface IOAuthAPI {

    @PostMapping("/register")
    String registerUser();

    @PostMapping("/login/user")
    @GenerateUserTokenRouteDescription
    Mono<String> generateUserToken();

    @PostMapping("/login/admin")
    @GenerateAdminTokenRouteDescription
    String generateAdminToken();

}

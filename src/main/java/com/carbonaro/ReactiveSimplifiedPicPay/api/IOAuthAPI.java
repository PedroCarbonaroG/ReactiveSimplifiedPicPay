package com.carbonaro.ReactiveSimplifiedPicPay.api;

import com.carbonaro.ReactiveSimplifiedPicPay.api.annotations.route_description.GenerateUserTokenRouteDescription;
import com.carbonaro.ReactiveSimplifiedPicPay.api.responses.oauth.TokenResponse;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.SystemUser;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@Tag(name = "OAuthAPI - User authentication & Token generation")
@RequestMapping(value = "/oauth")
public interface IOAuthAPI {

    @PostMapping("/register")
    Mono<SystemUser> registerUser(@RequestHeader String username, @RequestHeader String password);

    @PostMapping("/login/user")
    @GenerateUserTokenRouteDescription
    Mono<TokenResponse> generateUserToken(@RequestHeader String username, @RequestHeader String password);

}

package com.carbonaro.ReactiveSimplifiedPicPay.api;

import com.carbonaro.ReactiveSimplifiedPicPay.api.annotations.route_description.GenerateTokenRouteDescription;
import com.carbonaro.ReactiveSimplifiedPicPay.api.annotations.route_description.RegisterAdminRouteDescription;
import com.carbonaro.ReactiveSimplifiedPicPay.api.annotations.route_description.RegisterUserRouteDescription;
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
    @RegisterUserRouteDescription
    Mono<SystemUser> registerUser(@RequestHeader String username, @RequestHeader String password);

    @PostMapping("/register/admin")
    @RegisterAdminRouteDescription
    Mono<SystemUser> registerAdmin(@RequestHeader String username, @RequestHeader String password);

    @PostMapping("/login")
    @GenerateTokenRouteDescription
    Mono<TokenResponse> generateToken(@RequestHeader String username, @RequestHeader String password);

}

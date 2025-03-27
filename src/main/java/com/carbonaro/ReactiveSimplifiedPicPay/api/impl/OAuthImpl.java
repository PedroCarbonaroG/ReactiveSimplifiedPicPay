package com.carbonaro.ReactiveSimplifiedPicPay.api.impl;

import com.carbonaro.ReactiveSimplifiedPicPay.api.IOAuthAPI;
import com.carbonaro.ReactiveSimplifiedPicPay.api.responses.oauth.TokenResponse;
import com.carbonaro.ReactiveSimplifiedPicPay.core.security.JwtHandler;
import com.carbonaro.ReactiveSimplifiedPicPay.core.security.SecurityConfig;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.SystemUser;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.enums.SystemUserEnum;
import com.carbonaro.ReactiveSimplifiedPicPay.services.SystemUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import java.util.Set;

import static com.carbonaro.ReactiveSimplifiedPicPay.AppConstants.OAUTH_USER_NOT_FOUND;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OAuthImpl implements IOAuthAPI {

    private final SystemUserService userService;
    private final JwtHandler jwtHandler;

    @Override
    public Mono<SystemUser> registerUser(String username, String password) {

        var user = SystemUser.builder()
                .username(username)
                .password(SecurityConfig.passwordEncoder().encode(password))
                .authorities(Set.of(new SimpleGrantedAuthority(SystemUserEnum.USER.getValue())))
                .build();

        return userService.save(user);
    }

    @Override
    public Mono<TokenResponse> generateToken(String username, String password) {

        return userService
                .findByUsername(username)
                .switchIfEmpty(Mono.error(new SecurityException(OAUTH_USER_NOT_FOUND)))
                .map(jwtHandler::generateToken);
    }

}
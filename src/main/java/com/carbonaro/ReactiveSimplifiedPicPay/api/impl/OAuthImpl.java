package com.carbonaro.ReactiveSimplifiedPicPay.api.impl;

import com.carbonaro.ReactiveSimplifiedPicPay.api.IOAuthAPI;
import com.carbonaro.ReactiveSimplifiedPicPay.core.security.JwtHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OAuthImpl implements IOAuthAPI {

    private final PasswordEncoder passwordEncoder;
    private final ReactiveUserDetailsService userDetailsService;
    private final JwtHandler jwtHandler;

    @Override
    public String registerUser() {
        return "";
    }

    @Override
    public Mono<String> generateUserToken() {

        String username = "user";
        String password = "password";

        return userDetailsService.findByUsername(username)
                .filter(u -> passwordEncoder.matches(password, u.getPassword()))
                .map(jwtHandler::generateToken);
    }

    @Override
    public String generateAdminToken() {
        return null;
    }

}
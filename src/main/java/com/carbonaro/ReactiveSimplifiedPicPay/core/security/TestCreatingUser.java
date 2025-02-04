package com.carbonaro.ReactiveSimplifiedPicPay.core.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class TestCreatingUser {

    @Bean
    MapReactiveUserDetailsService userDetailsService() {

        var passwordEncoder = new BCryptPasswordEncoder();

        var user = User.builder()
                .username("user")
                .password(passwordEncoder.encode("password"))
                .roles("USER")
                .build();
        return new MapReactiveUserDetailsService(user);
    }

}

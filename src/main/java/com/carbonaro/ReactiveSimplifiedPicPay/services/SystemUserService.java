package com.carbonaro.ReactiveSimplifiedPicPay.services;

import com.carbonaro.ReactiveSimplifiedPicPay.core.security.SecurityConfig;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.SystemUser;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.enums.SystemUserEnum;
import com.carbonaro.ReactiveSimplifiedPicPay.repositories.SystemUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SystemUserService {

    private final SystemUserRepository userRepository;

    public Mono<SystemUser> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Mono<SystemUser> createUser(String username, String password) {

        var user = SystemUser.builder()
                .username(username)
                .password(SecurityConfig.passwordEncoder().encode(password))
                .authorities(Set.of(new SimpleGrantedAuthority(SystemUserEnum.USER.getValue())))
                .build();

        return userRepository.save(user);
    }

}

package com.carbonaro.ReactiveSimplifiedPicPay.services;

import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.SystemUser;
import com.carbonaro.ReactiveSimplifiedPicPay.repositories.SystemUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class SystemUserService {

    private final SystemUserRepository userRepository;

    public Mono<SystemUser> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Mono<SystemUser> save(SystemUser user) {
        return userRepository.save(user);
    }

}

package com.carbonaro.ReactiveSimplifiedPicPay.repositories;

import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.SystemUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class SystemUserRepository extends BaseRepository {

    private final ISystemUserRepository userRepository;

    public Mono<SystemUser> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Mono<SystemUser> save(SystemUser user) {
        return userRepository.save(user);
    }

}

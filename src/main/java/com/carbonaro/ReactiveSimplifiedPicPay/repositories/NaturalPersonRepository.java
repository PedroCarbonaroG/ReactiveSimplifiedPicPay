package com.carbonaro.ReactiveSimplifiedPicPay.repositories;

import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.NaturalPerson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class NaturalPersonRepository extends BaseRepository {

    private final INaturalPersonRepository repository;

    public Mono<NaturalPerson> findByCpf(String document) {
        return repository.findByCpf(document);
    }

    public Mono<Void> deleteAll() {
        return repository.deleteAll();
    }

    public Flux<NaturalPerson> saveAll(List<NaturalPerson> naturals) {
        return repository.saveAll(naturals);
    }

}

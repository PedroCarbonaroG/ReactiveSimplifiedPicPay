package com.carbonaro.ReactiveSimplifiedPicPay.repositories;

import com.carbonaro.ReactiveSimplifiedPicPay.api.requests.person.NaturalPersonFilterRequest;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.NaturalPerson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Slf4j
@Repository
@RequiredArgsConstructor
public class NaturalPersonRepository extends BaseRepository {

    private final INaturalPersonRepository repository;

    private static final String NAME = "name";
    private static final String EMAIL = "email";
    private static final String ADDRESS = "address";
    private static final String BIRTH_DATE = "birthDate";

    public Mono<Page<NaturalPerson>> findAll(Pageable page, NaturalPersonFilterRequest request) {

        Query query = new Query();

        addParamToQuery(query, NAME, request.getName());
        addParamToQuery(query, EMAIL, request.getEmail());
        addParamToQuery(query, ADDRESS, request.getEmail());
        addParamBetweenDates(query, BIRTH_DATE,  request.getBirthDate(), request.getBirthDate());

        return toPage(query, page, NaturalPerson.class);
    }

    public Mono<NaturalPerson> findById(String id) {
        return repository.findById(id);
    }

    public Mono<NaturalPerson> findByCpf(String document) {
        return repository.findByCpf(document);
    }

    public Mono<Void> deleteByCpf(String document) {
        return repository.deleteByCpf(document);
    }

    public Mono<Void> delete(NaturalPerson natural) {
        return repository.delete(natural);
    }

    public Mono<Void> deleteAll() {
        return repository.deleteAll();
    }

    public Mono<NaturalPerson> save(NaturalPerson naturalPerson) {
        return repository.save(naturalPerson);
    }

    public Flux<NaturalPerson> saveAll(Iterable<NaturalPerson> naturals) {
        return repository.saveAll(naturals);
    }

}

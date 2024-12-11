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
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class NaturalPersonRepository extends BaseRepository {

    private final INaturalPersonRepository repository;

    private static final String CPF = "cpf";
    private static final String NAME = "name";
    private static final String EMAIL = "email";
    private static final String ADDRESS = "address";
    private static final String BIRTH_DATE = "birthDate";

    public Mono<Page<NaturalPerson>> listAllNaturals(Pageable pageRequest, NaturalPersonFilterRequest filterRequest) {

        Query query = new Query();

        addParam(filterRequest.getCpf(), CPF, query);
        addParam(filterRequest.getName(), NAME, query);
        addParam(filterRequest.getEmail(), EMAIL, query);
        addParam(filterRequest.getAddress(), ADDRESS, query);
        addParamBetweenDates(filterRequest.getInitialBirthDate(), filterRequest.getFinalBirthDate(), BIRTH_DATE, query);

        return toPage(query, pageRequest, NaturalPerson.class);
    }

    public Mono<NaturalPerson> findById(String id) {
        return repository.findById(id);
    }

    public Mono<NaturalPerson> findByCpf(String document) {
        return repository.findByCpf(document);
    }

    public Mono<Void> deleteAll() {
        return repository.deleteAll();
    }

    public Mono<NaturalPerson> save(NaturalPerson natural) {
        return repository.save(natural);
    }

    public Flux<NaturalPerson> saveAll(List<NaturalPerson> naturals) {
        return repository.saveAll(naturals);
    }

}

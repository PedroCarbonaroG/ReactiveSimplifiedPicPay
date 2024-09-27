package com.carbonaro.ReactiveSimplifiedPicPay.repositories;

import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.NaturalPerson;
import jdk.jfr.Description;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface NaturalPersonRepository extends ReactiveMongoRepository<NaturalPerson, String> {

    @Description("Returns a NaturalPerson by it's unique CPF")
    Mono<NaturalPerson> findByCpf(String cpf);

    @Description("Successfully delete an NaturalPerson by it's CPF")
    Mono<Void> deleteByCpf(String cpf);

}

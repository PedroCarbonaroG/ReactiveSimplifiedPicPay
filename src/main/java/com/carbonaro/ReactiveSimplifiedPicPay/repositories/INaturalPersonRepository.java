package com.carbonaro.ReactiveSimplifiedPicPay.repositories;

import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.NaturalPerson;
import jdk.jfr.Description;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
interface INaturalPersonRepository extends ReactiveMongoRepository<NaturalPerson, String> {

    @Description("Returns a NaturalPerson by it's unique CPF")
    Mono<NaturalPerson> findByCpf(String cpf);

    @Description("Returns a NaturalPerson by your unique email")
    Mono<NaturalPerson> findByEmail(String email);

    @Description("Successfully delete an NaturalPerson by it's CPF")
    Mono<Void> deleteByCpf(String cpf);

}

package com.carbonaro.ReactiveSimplifiedPicPay.repositories;

import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.NaturalPerson;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface NaturalPersonRepository extends ReactiveMongoRepository<NaturalPerson, String> {

    @Schema(description = "Retornar um NaturalPerson por meio do seu nome completo.")
    Mono<NaturalPerson> findByName(String name);

    @Schema(description = "Retornar um NaturalPerson por meio do seu CPF.")
    Mono<NaturalPerson> findByCpf(String cpf);
}

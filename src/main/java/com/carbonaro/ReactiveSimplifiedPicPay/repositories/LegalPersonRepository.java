package com.carbonaro.ReactiveSimplifiedPicPay.repositories;

import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.LegalPerson;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface LegalPersonRepository extends ReactiveMongoRepository<LegalPerson, String> {

    @Schema(description = "Retornar um LegalPerson por meio do seu CNPJ.")
    Mono<LegalPerson> findByCnpj(String cnpj);
}

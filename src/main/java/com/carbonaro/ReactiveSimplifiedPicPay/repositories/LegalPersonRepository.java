package com.carbonaro.ReactiveSimplifiedPicPay.repositories;

import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.LegalPerson;
import jdk.jfr.Description;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface LegalPersonRepository extends ReactiveMongoRepository<LegalPerson, String> {

    @Description("Returns a LegalPerson by it's unique CNPJ.")
    Mono<LegalPerson> findByCnpj(String cnpj);

}

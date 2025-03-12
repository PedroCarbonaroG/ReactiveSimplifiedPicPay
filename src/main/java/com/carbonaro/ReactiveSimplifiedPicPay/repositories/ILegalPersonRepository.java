package com.carbonaro.ReactiveSimplifiedPicPay.repositories;

import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.LegalPerson;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.NaturalPerson;
import jdk.jfr.Description;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
interface ILegalPersonRepository extends ReactiveMongoRepository<LegalPerson, String> {

    @Description("Returns a LegalPerson by it's unique CNPJ.")
    Mono<LegalPerson> findByCnpj(String cnpj);

    @Description("Returns a LegalPerson by your unique email")
    Mono<LegalPerson> findByEmail(String email);

    @Description("Deletes a LegalPerson by it's unique CNPJ.")
    Mono<Void> deleteByCnpj(String cnpj);

}

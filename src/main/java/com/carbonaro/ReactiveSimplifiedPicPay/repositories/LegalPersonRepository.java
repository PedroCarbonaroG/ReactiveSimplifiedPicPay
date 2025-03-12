package com.carbonaro.ReactiveSimplifiedPicPay.repositories;

import com.carbonaro.ReactiveSimplifiedPicPay.api.requests.person.LegalPersonFilterRequest;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.LegalPerson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@Slf4j
@Repository
@RequiredArgsConstructor
public class LegalPersonRepository extends BaseRepository {

    private final ILegalPersonRepository repository;

    public static final String CNPJ = "cnpj";
    public static final String NAME = "name";
    public static final String ADDRESS = "address";
    public static final String COMPANY_SIZE = "companySize";
    public static final String EMPLOYEES_NUMBER = "employeesNumber";
    public static final String MONTHLY_BILLING = "monthlyBilling";
    public static final String ANNUAL_BILLING = "annualBilling";

    public Mono<Page<LegalPerson>> findAll(Pageable page, LegalPersonFilterRequest filterRequest) {

        Query query = new Query();

        addParamToQuery(query, CNPJ, filterRequest.getCnpj());
        addParamToQuery(query, NAME, filterRequest.getName());
        addParamToQuery(query, ADDRESS, filterRequest.getAddress());
        addParamToQuery(query, COMPANY_SIZE, filterRequest.getCompanySize());
        addParamToQuery(query, EMPLOYEES_NUMBER, filterRequest.getEmployeesNumber());
        addParamBetweenValues(query, MONTHLY_BILLING, filterRequest.getInitialMonthlyBilling(), filterRequest.getFinalMonthlyBilling());
        addParamBetweenValues(query, ANNUAL_BILLING, filterRequest.getInitialAnnualBilling(), filterRequest.getFinalAnnualBilling());

        return toPage(query, page, LegalPerson.class);
    }

    public Mono<LegalPerson> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    public Mono<LegalPerson> findById(String id) {
        return repository.findById(id);
    }

    public Mono<LegalPerson> findByCnpj(String cnpj) {
        return repository.findByCnpj(cnpj);
    }

    public Mono<Void> deleteByCnpj(String cnpj) {
        return repository.deleteByCnpj(cnpj);
    }

    public Mono<Void> deleteAll() {
        return repository.deleteAll();
    }

    public Mono<Void> save(LegalPerson legalPerson) {
        return repository.save(legalPerson).then();
    }

    public Flux<LegalPerson> saveAll(Iterable<LegalPerson> legals) {
        return repository.saveAll(legals);
    }

    public Mono<LegalPerson> update(Tuple2<LegalPerson, LegalPerson> tupleOfLegals) {
        return Mono.just(copyPropertiesForNullFields(tupleOfLegals.getT1(), tupleOfLegals.getT2())).flatMap(repository::save);
    }

}

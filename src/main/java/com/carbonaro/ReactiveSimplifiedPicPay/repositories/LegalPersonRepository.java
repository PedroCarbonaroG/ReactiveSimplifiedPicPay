package com.carbonaro.ReactiveSimplifiedPicPay.repositories;

import com.carbonaro.ReactiveSimplifiedPicPay.api.requests.person.LegalPersonFilterRequest;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.LegalPerson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class LegalPersonRepository extends BaseRepository {

    private final ILegalPersonRepository repository;

    private static final String CNPJ = "cnpj";
    private static final String NAME = "name";
    private static final String ADDRESS = "address";
    private static final String COMPANY_SIZE = "companySize";
    private static final String EMPLOYEES_NUMBER = "employeesNumber";
    private static final String MONTHLY_BILLING = "monthlyBilling";
    private static final String ANNUAL_BILLING = "annualBilling";

    public Mono<Page<LegalPerson>> listAll(Pageable page, LegalPersonFilterRequest filterRequest) {

        Query query = new Query();

        addParam(filterRequest.getCnpj(), CNPJ, query);
        addParam(filterRequest.getName(), NAME, query);
        addParam(filterRequest.getAddress(), ADDRESS, query);
        addParam(filterRequest.getCompanySize(), COMPANY_SIZE, query);
        addParam(filterRequest.getEmployeesNumber(), EMPLOYEES_NUMBER, query);
        addParamValues(filterRequest.getInitialAnnualBilling(), filterRequest.getFinalAnnualBilling(), MONTHLY_BILLING, query);
        addParamValues(filterRequest.getInitialMonthlyBilling(), filterRequest.getFinalMonthlyBilling(), ANNUAL_BILLING, query);

        return toPage(query, page, LegalPerson.class);
    }

    public Mono<LegalPerson> save(LegalPerson legalPerson) {
        return repository.save(legalPerson);
    }

    public Mono<LegalPerson> findById(String id) {
        return repository.findById(id);
    }

    public Mono<LegalPerson> findByCnpj(String document) {
        return repository.findByCnpj(document);
    }

    public Mono<Void> deleteByCnpj(String document) {
        return repository.deleteByCnpj(document);
    }

    public Mono<Void> deleteAll() {
        return repository.deleteAll();
    }

    public Flux<LegalPerson> saveAll(List<LegalPerson> legals) {
        return repository.saveAll(legals);
    }

    public Mono<LegalPerson> update(Tuple2<LegalPerson, LegalPerson> tupleOfLegals) {

        return Mono.just(tupleOfLegals)
                .map(self -> copyPropertiesForNullFields(self.getT1(), self.getT2()))
                .flatMap(repository::save);
    }

}

package com.carbonaro.ReactiveSimplifiedPicPay.services;

import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.LegalPerson;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.NaturalPerson;
import com.carbonaro.ReactiveSimplifiedPicPay.repositories.LegalPersonRepository;
import com.carbonaro.ReactiveSimplifiedPicPay.services.exceptions.EmptyReturnException;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static com.carbonaro.ReactiveSimplifiedPicPay.AppConstants.GENERAL_WARNING_EMPTY;
import static com.carbonaro.ReactiveSimplifiedPicPay.AppConstants.LEGAL_DELETE_PARTNER_NOT_FOUND_ERROR;

@Slf4j
@Service
@AllArgsConstructor
public class LegalPersonService {

    private final LegalPersonRepository repositoryLP;
    private final ReactiveMongoTemplate mongoTemplate;
    private final NaturalPersonService naturalPersonService;

    public Mono<Void> saveLegalPerson(LegalPerson legalPerson) {

        return Mono
                .just(legalPerson)
                .flatMap(this::validateLegalPerson)
                .flatMap(repositoryLP::save)
                .doOnSuccess(person -> log.info("New LegalPerson was persisted with success!"))
                .doOnError(errorResponse -> Mono.error(new Exception(errorResponse.getMessage())))
                .then();
    }

    public Mono<Void> deleteLegal(String companyCNPJ) {

        return findLegalByCNPJ(companyCNPJ)
                .map(company -> repositoryLP.deleteByCnpj(company.getCnpj()).subscribe())
                .doOnSuccess(unused -> log.info("Company with CNPJ: {}, was deleted with success!", companyCNPJ))
                .then();
    }

    public Mono<Void> updateLegalPerson(String cnpj, LegalPerson legalPerson) {

        log.info("LegalPerson | Updating Legal");
        return Mono
                .just(legalPerson)
                .flatMap(this::validateLegalPersonFields)
                .flatMap(this::buildFields)
                .flatMap(response -> this.buildQuery(response, cnpj, legalPerson));
    }
    private Mono<List<Field>> buildFields(LegalPerson legalPerson) {

        return Mono.just(Stream
                .concat(
                        Arrays.stream(legalPerson.getClass().getDeclaredFields()),
                        Arrays.stream(legalPerson.getClass().getSuperclass().getDeclaredFields()))
                .filter(field -> {
                    try {
                        field.setAccessible(true);
                        return Objects.nonNull(field.get(legalPerson)) && !field.get(legalPerson).toString().isEmpty();
                    } catch (IllegalAccessException e) {
                        throw new DataIntegrityViolationException(e.getMessage());
                    }})
                .toList());
    }
    @SneakyThrows
    private Mono<Void> buildQuery(List<Field> fields, String cnpj, LegalPerson legalPerson) {

        Query query = new Query(Criteria.where("cnpj").is(cnpj));
        Update update = new Update();

        for (Field field : fields) update.set(field.getName(), field.get(legalPerson));

        return mongoTemplate
                .updateFirst(query, update, LegalPerson.class)
                .then();
    }

    public Mono<Void> savePartner(String cnpj, String partnerCPF) {

        log.info("LegalPerson | Saving new partner for company: {}", cnpj);
        return Mono
                .zip(this.findLegalByCNPJ(cnpj), naturalPersonService.findNaturalByCPF(partnerCPF))
                .flatMap(this::savePartner)
                .then();
    }
    private Mono<Void> savePartner(Tuple2<LegalPerson, NaturalPerson> tuple) {

        return Mono
                .just(tuple)
                .flatMap(self -> {

                    if (self.getT1().getPartners().stream().anyMatch(it -> it.getCpf().equals(tuple.getT2().getCpf()))) {
                        return Mono.error(new DataIntegrityViolationException("Already have this partner in the company"));
                    } else {
                        self.getT1().getPartners().add(self.getT2());
                    }

                    return this.saveLegalPerson(self.getT1());
                });
    }

    public Mono<Void> deletePartner(String companyCNPJ, String partnerCPF) {

        log.info("LegalPerson | Deleting partner: {}, from company: {}", partnerCPF, companyCNPJ);
        return Mono
                .zip(
                        findLegalByCNPJ(companyCNPJ),
                        naturalPersonService.findNaturalByCPF(partnerCPF))
                .flatMap(tuple -> removePartner(tuple.getT1(), tuple.getT2().getCpf()))
                .map(updatedCompany -> repositoryLP.save(updatedCompany).subscribe())
                .doOnSuccess(unused -> log.info("Partner of CPF: {}, was deleted with success from the company with CNPJ: {}", partnerCPF, companyCNPJ))
                .then();
    }
    private Mono<LegalPerson> removePartner(LegalPerson company, String partnerCPF) {

        return company
                .getPartners()
                .removeIf(partner -> partner.getCpf().equals(partnerCPF))
                ? Mono.just(company)
                : Mono.error(new EmptyReturnException(LEGAL_DELETE_PARTNER_NOT_FOUND_ERROR));
    }

    public Flux<LegalPerson> findAllLegals() {

        return repositoryLP
                .findAll()
                .switchIfEmpty(Flux.error(new EmptyReturnException(GENERAL_WARNING_EMPTY)))
                .doOnError(errorResponse -> Flux.error(new Exception(errorResponse.getMessage())))
                .doOnComplete(() -> log.info("Legals list was deployed with success!"));
    }

    public Mono<LegalPerson> findLegalById(String id) {

        return repositoryLP
                .findById(id)
                .switchIfEmpty(Mono.error(new EmptyReturnException(GENERAL_WARNING_EMPTY)))
                .doOnError(errorResponse -> Mono.error(new Exception(errorResponse.getMessage())));
    }

    public Mono<LegalPerson> findLegalByCNPJ(String cnpj) {

        return repositoryLP
                .findByCnpj(cnpj)
                .switchIfEmpty(Mono.error(new EmptyReturnException(GENERAL_WARNING_EMPTY)))
                .doOnError(errorResponse -> Mono.error(new Exception(errorResponse.getMessage())));
    }

    private Mono<LegalPerson> validateLegalPerson(LegalPerson legalPerson) {

        String onlyNumbers = "\\d+";
        return Mono
                .just(legalPerson)
                .flatMap(unused -> repositoryLP
                            .findByCnpj(legalPerson.getCnpj())
                            .hasElement()
                            .flatMap(hasValue -> Boolean.FALSE.equals(hasValue) ? Mono.just(legalPerson) : Mono.error(new DataIntegrityViolationException("Already have a LegalPerson with that CNPJ.")))
                )
                .flatMap(unused -> legalPerson.getCnpj().matches(onlyNumbers) && legalPerson.getCnpj().length() == 14
                            ? Mono.just(legalPerson)
                            : Mono.error(new DataIntegrityViolationException("LegalPerson CNPJ contains more than 14 numbers or don't have only numbers"))
                );
    }

    private Mono<LegalPerson> validateLegalPersonFields(LegalPerson legalPerson) {

        return Stream
                .concat(
                        Arrays.stream(legalPerson.getClass().getDeclaredFields()),
                        Arrays.stream(legalPerson.getClass().getSuperclass().getDeclaredFields()))
                .filter(field -> {
                    try {
                        field.setAccessible(true);
                        return Objects.nonNull(field.get(legalPerson)) && !field.get(legalPerson).toString().isEmpty() && field.get(legalPerson).toString().length() != 1;
                    } catch (IllegalAccessException e) {
                        throw new DataIntegrityViolationException(e.getMessage());
                    }})
                .toList()
                .isEmpty()

                ? Mono.error(new Exception())
                : Mono.just(legalPerson);
    }

}
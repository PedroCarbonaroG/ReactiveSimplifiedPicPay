package com.carbonaro.ReactiveSimplifiedPicPay.services;

import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.LegalPerson;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.NaturalPerson;
import com.carbonaro.ReactiveSimplifiedPicPay.repositories.LegalPersonRepository;
import com.carbonaro.ReactiveSimplifiedPicPay.services.exceptions.BadRequestException;
import com.carbonaro.ReactiveSimplifiedPicPay.services.exceptions.EmptyException;
import com.carbonaro.ReactiveSimplifiedPicPay.services.exceptions.EmptyOrNullObjectException;
import com.carbonaro.ReactiveSimplifiedPicPay.services.exceptions.NotFoundException;
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
                .switchIfEmpty(Mono.error(new EmptyOrNullObjectException()))
                .flatMap(this::validateLegalPerson)
                .flatMap(repositoryLP::save)
                .doOnSuccess(person -> log.info("New LegalPerson was persisted with success!"))
                .doOnError(errorResponse -> Mono.error(new Exception(errorResponse.getMessage())))
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

    public Mono<Void> deletePartner(String cnpj, String partnerCNPJ) {

        log.info("LegalPerson | Deleting partner: {}, from company: {}", partnerCNPJ, cnpj);
        return Mono
                .zip(this.findLegalByCNPJ(cnpj), naturalPersonService.findNaturalByCPF(partnerCNPJ))
                .flatMap(this::deletePartner)
                .then();
    }
    private Mono<Void> deletePartner(Tuple2<LegalPerson, NaturalPerson> tuple) {

        return Mono
                .just(tuple)
                .flatMap(self -> self.getT1().getPartners().removeIf(it -> it.getCpf().equals(self.getT2().getCpf()))
                        ? this.saveLegalPerson(tuple.getT1())
                        : Mono.error(new NotFoundException("Don't exist any partner with that CPF in this company.")));
    }

    public Flux<LegalPerson> findAllLegals() {

        return repositoryLP
                .findAll()
                .switchIfEmpty(Flux.error(new EmptyException()))
                .doOnError(errorResponse -> Flux.error(new Exception(errorResponse.getMessage())))
                .doOnComplete(() -> log.info("Legals list was deployed with success!"));
    }

    public Mono<LegalPerson> findLegalById(String id) {

        return repositoryLP
                .findById(id)
                .switchIfEmpty(Mono.error(new EmptyException()))
                .doOnError(errorResponse -> Mono.error(new Exception(errorResponse.getMessage())));
    }

    public Mono<LegalPerson> findLegalByCNPJ(String cnpj) {

        return repositoryLP
                .findByCnpj(cnpj)
                .switchIfEmpty(Mono.error(new EmptyException()))
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

                ? Mono.error(new BadRequestException())
                : Mono.just(legalPerson);
    }

}
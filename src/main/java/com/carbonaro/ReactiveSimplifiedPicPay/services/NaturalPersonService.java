package com.carbonaro.ReactiveSimplifiedPicPay.services;

import com.carbonaro.ReactiveSimplifiedPicPay.api.requests.NaturalPersonFilterRequest;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.NaturalPerson;
import com.carbonaro.ReactiveSimplifiedPicPay.repositories.NaturalPersonRepository;
import com.carbonaro.ReactiveSimplifiedPicPay.services.exceptions.BadRequestException;
import com.carbonaro.ReactiveSimplifiedPicPay.services.exceptions.EmptyException;
import java.math.BigDecimal;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Service
@RequiredArgsConstructor
public class NaturalPersonService {

    private final NaturalPersonRepository repositoryNP;

    public Mono<Page<NaturalPerson>> findAllNaturals(Pageable page, NaturalPersonFilterRequest filterRequest) {

        return repositoryNP
                .findAll(page, filterRequest)
                .switchIfEmpty(Mono.error(new EmptyException()))
                .doOnError(errorResponse -> Flux.error(new Exception(errorResponse.getMessage())));
    }

    public Mono<NaturalPerson> findNaturalById(String id) {

        return repositoryNP
                .findById(id)
                .switchIfEmpty(Mono.error(new EmptyException()))
                .doOnError(errorResponse -> Mono.error(new Exception(errorResponse.getMessage())));
    }

    public Mono<NaturalPerson> findNaturalByCPF(String cpf) {

        return repositoryNP
                .findByCpf(cpf)
                .switchIfEmpty(Mono.error(new EmptyException()))
                .doOnError(errorResponse -> Mono.error(new Exception(errorResponse.getMessage())));
    }

    public Mono<Void> saveNaturalPerson(NaturalPerson naturalPerson) {

        return Mono.just(naturalPerson)
                .flatMap(self -> {
                    self.setCpf(self.getCpf());
                    return repositoryNP.findByCpf(self.getCpf());
                })
                .flatMap(alreadyExistentNatural -> Mono.error(new BadRequestException()))
                .switchIfEmpty(validateNewNaturalPerson(naturalPerson)
                        .publishOn(Schedulers.boundedElastic())
                        .map(newNatural -> repositoryNP.save(newNatural).subscribe())
                        .doOnSuccess(unused -> log.info("NaturalPerson | Saving new natural with CPF: {}", naturalPerson.getCpf())))
                .doOnError(errorResponse -> Mono.error(new Exception(errorResponse.getMessage())))
                .then();
    }
    private Mono<NaturalPerson> validateNewNaturalPerson(NaturalPerson naturalPerson) {

        return Mono
                .just(naturalPerson)
                .flatMap(self -> (Objects.nonNull(self.getCpf()) && !self.getCpf().isEmpty()) &&
                        (Objects.nonNull(self.getBirthDate()) && !self.getBirthDate().toString().isEmpty()) &&
                        (Objects.nonNull(self.getName()) && !self.getName().isEmpty()) &&
                        (Objects.nonNull(self.getEmail()) && !self.getEmail().isEmpty()) &&
                        (Objects.nonNull(self.getAddress()) && !self.getAddress().isEmpty()) &&
                        (Objects.nonNull(self.getPassword()) && !self.getPassword().isEmpty())
                        ? Mono.just(self)
                        : Mono.error(new BadRequestException()));
    }

    public Mono<Void> updateNaturalPerson(NaturalPerson naturalPerson, String cpf) {

        return this
                .fillEmptyFieldsIfHas(naturalPerson, cpf)
                .flatMap(self -> repositoryNP.deleteByCpf(cpf).thenReturn(self))
                .flatMap(repositoryNP::save)
                .doOnSuccess(unused -> log.info("NaturalPerson was updated with success!"))
                .doOnError(errorResponse -> Mono.error(new Exception(errorResponse.getMessage())))
                .then();
    }
    private Mono<NaturalPerson> fillEmptyFieldsIfHas(NaturalPerson naturalPerson, String cpf) {

        return this
                .findNaturalByCPF(cpf)
                .zipWith(Mono.just(naturalPerson))
                .map(tuple -> NaturalPerson.builder()
                        .id(tuple.getT1().getId())
                        .cpf(tuple.getT1().getCpf())
                        .balance(tuple.getT1().getBalance())
                        .birthDate(Objects.nonNull(naturalPerson.getBirthDate()) && naturalPerson.getBirthDate().toString().isEmpty() ? tuple.getT1().getBirthDate() : tuple.getT2().getBirthDate())
                        .name(Objects.nonNull(naturalPerson.getName()) && naturalPerson.getName().isEmpty() ? tuple.getT1().getName() : tuple.getT2().getName())
                        .email(Objects.nonNull(naturalPerson.getEmail()) && naturalPerson.getEmail().isEmpty() ? tuple.getT1().getEmail() : tuple.getT2().getEmail())
                        .address(Objects.nonNull(naturalPerson.getAddress()) && naturalPerson.getAddress().isEmpty() ? tuple.getT1().getAddress() : tuple.getT2().getAddress())
                        .password(Objects.nonNull(naturalPerson.getPassword()) && naturalPerson.getPassword().isEmpty() ? tuple.getT1().getPassword() : tuple.getT2().getPassword())
                        .build());
    }

    public Mono<Void> deleteNatural(String cpf) {

        return repositoryNP
                .deleteByCpf(cpf)
                .then();
    }

    public Mono<Void> deposit(NaturalPerson naturalPerson, BigDecimal amount) {

        return Mono.just(naturalPerson)
                .flatMap(self -> {
                    self.setBalance(self.getBalance().add(amount));
                    return repositoryNP.save(self);
                })
                .doOnError(errorResponse -> Mono.error(new Exception(errorResponse.getMessage())))
                .then();
    }

}

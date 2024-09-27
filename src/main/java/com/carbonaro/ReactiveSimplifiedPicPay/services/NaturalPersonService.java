package com.carbonaro.ReactiveSimplifiedPicPay.services;

import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.NaturalPerson;
import com.carbonaro.ReactiveSimplifiedPicPay.repositories.NaturalPersonRepository;
import com.carbonaro.ReactiveSimplifiedPicPay.services.exceptions.BadRequestException;
import com.carbonaro.ReactiveSimplifiedPicPay.services.exceptions.EmptyReturnException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static com.carbonaro.ReactiveSimplifiedPicPay.AppConstants.GENERAL_WARNING_EMPTY;

@Slf4j
@Service
@AllArgsConstructor
public class NaturalPersonService {

    private final NaturalPersonRepository repositoryNP;

    public Flux<NaturalPerson> findAllNaturals() {

        return repositoryNP
                .findAll()
                .switchIfEmpty(Flux.error(new EmptyReturnException(GENERAL_WARNING_EMPTY)))
                .doOnError(errorResponse -> Flux.error(new Exception(errorResponse.getMessage())))
                .doOnComplete(() -> log.info("Naturals list was deployed with success!"));
    }

    public Mono<NaturalPerson> findNaturalById(String id) {

        return repositoryNP
                .findById(id)
                .switchIfEmpty(Mono.error(new EmptyReturnException(GENERAL_WARNING_EMPTY)))
                .doOnError(errorResponse -> Mono.error(new Exception(errorResponse.getMessage())));
    }

    public Mono<NaturalPerson> findNaturalByCPF(String cpf) {

        return repositoryNP
                .findByCpf(cpf)
                .switchIfEmpty(Mono.error(new EmptyReturnException(GENERAL_WARNING_EMPTY)))
                .doOnError(errorResponse -> Mono.error(new Exception(errorResponse.getMessage())));
    }

    public Mono<Void> saveNaturalPerson(NaturalPerson naturalPerson) {

        naturalPerson.setCpf("");
        return repositoryNP
                .findByCpf("")
                .map(self -> Objects.nonNull(self) ? naturalPerson : Mono.error(new BadRequestException("This NaturalPerson already exists!")))
                .flatMap(naturalPersonNotRegistered -> this.validateNewNaturalPerson((NaturalPerson) naturalPersonNotRegistered))
                .flatMap(repositoryNP::save)
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
                        : Mono.error(new Exception()));
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
}

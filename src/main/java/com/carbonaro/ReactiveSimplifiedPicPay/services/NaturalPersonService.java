package com.carbonaro.ReactiveSimplifiedPicPay.services;

import com.carbonaro.ReactiveSimplifiedPicPay.api.exception_handler.helper.MessageHelper;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.NaturalPerson;
import com.carbonaro.ReactiveSimplifiedPicPay.repositories.NaturalPersonRepository;
import com.carbonaro.ReactiveSimplifiedPicPay.services.exceptions.BadRequestException;
import com.carbonaro.ReactiveSimplifiedPicPay.services.exceptions.EmptyReturnException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import java.util.Objects;

import static com.carbonaro.ReactiveSimplifiedPicPay.AppConstants.*;

@Slf4j
@Service
@AllArgsConstructor
public class NaturalPersonService {

    private final MessageHelper messageHelper;
    private final NaturalPersonRepository repositoryNP;
    private final LegalPersonService legalPersonService;

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

    public Mono<Void> saveNatural(NaturalPerson naturalPerson, String newNaturalCPF) {

        return Mono.just(naturalPerson)
                .flatMap(self -> {
                    self.setCpf(newNaturalCPF);
                    return repositoryNP.findByCpf(self.getCpf());
                })
                .flatMap(alreadyExistentNatural -> Mono.error(new BadRequestException(messageHelper.getMessage(NATURAL_SAVE_ALREADY_EXISTS))))
                .switchIfEmpty(validateNewNatural(naturalPerson)
                        .publishOn(Schedulers.boundedElastic())
                        .map(newNatural -> repositoryNP.save(newNatural).subscribe())
                        .doOnSuccess(unused -> log.info("NaturalPerson | Saving new natural with CPF: {}", naturalPerson.getCpf())))
                .doOnError(errorResponse -> Mono.error(new Exception(errorResponse.getMessage())))
                .then();
    }
    private Mono<NaturalPerson> validateNewNatural(NaturalPerson naturalPerson) {

        return Mono.just(naturalPerson)
                .filter(self -> validateCPF(self.getCpf())
                        && validateField(self.getBirthDate())
                        && validateField(self.getName())
                        && validateField(self.getEmail())
                        && validateField(self.getAddress())
                        && validateField(self.getPassword()))
                .switchIfEmpty(Mono.error(new BadRequestException(messageHelper.getMessage(NATURAL_SAVE_INCORRECT_FIELD))));
    }
    private boolean validateCPF(String cpf) {

        String onlyNumbers = "\\d+";
        return Objects.nonNull(cpf) && BooleanUtils.isFalse(cpf.isEmpty()) && cpf.length() == 11 && cpf.matches(onlyNumbers);
    }

    public Mono<Void> updateNatural(NaturalPerson naturalPerson, String cpf) {

        return fillEmptyFieldsIfHas(naturalPerson, cpf)
                .publishOn(Schedulers.boundedElastic())
                .map(updatedNatural -> repositoryNP.save(updatedNatural).subscribe())
                .doOnSuccess(unused -> log.info("NaturalPerson was updated with success!"))
                .doOnError(errorResponse -> Mono.error(new Exception(errorResponse.getMessage())))
                .then();
    }
    private Mono<NaturalPerson> fillEmptyFieldsIfHas(NaturalPerson naturalPerson, String cpf) {

        return Mono
                .zip(findNaturalByCPF(cpf), Mono.just(naturalPerson))
                .map(tuple -> {
                    tuple.getT1().setBirthDate(validateField(tuple.getT2().getBirthDate()) ? tuple.getT2().getBirthDate() : tuple.getT1().getBirthDate());
                    tuple.getT1().setName(validateField(tuple.getT2().getName()) ? tuple.getT2().getName() : tuple.getT1().getName());
                    tuple.getT1().setEmail(validateField(tuple.getT2().getEmail()) ? tuple.getT2().getEmail() : tuple.getT1().getEmail());
                    tuple.getT1().setAddress(validateField(tuple.getT2().getAddress()) ? tuple.getT2().getAddress() : tuple.getT1().getAddress());
                    tuple.getT1().setPassword(validateField(tuple.getT2().getPassword()) ? tuple.getT2().getPassword() : tuple.getT1().getPassword());
                    return tuple.getT1();
                });
    }
    private <T> boolean validateField(T field) {

        return Objects.nonNull(field) && BooleanUtils.isFalse(field.toString().isEmpty());
    }

    public Mono<Void> deleteNatural(String cpf) {

        return findNaturalByCPF(cpf)
                .map(self -> repositoryNP.deleteByCpf(self.getCpf()).subscribe())
                .flatMap(unused -> legalPersonService.deletePartner(cpf))
                .doOnSuccess(unused -> log.info("Natural with CPF: {}, was deleted with success!", cpf))
                .then();
    }

}

package com.carbonaro.ReactiveSimplifiedPicPay.services;

import com.carbonaro.ReactiveSimplifiedPicPay.api.exception_handler.helper.MessageHelper;
import com.carbonaro.ReactiveSimplifiedPicPay.api.requests.person.NaturalPersonFilterRequest;
import com.carbonaro.ReactiveSimplifiedPicPay.api.responses.PageResponse;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.NaturalPerson;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.mappers.PageMapper;
import com.carbonaro.ReactiveSimplifiedPicPay.repositories.NaturalPersonRepository;
import com.carbonaro.ReactiveSimplifiedPicPay.services.exceptions.BadRequestException;
import com.carbonaro.ReactiveSimplifiedPicPay.services.exceptions.EmptyReturnException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import java.util.Objects;

import static com.carbonaro.ReactiveSimplifiedPicPay.AppConstants.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class NaturalPersonService {

    private final MessageHelper messageHelper;
    private final PageMapper<NaturalPerson> pageMapper;
    private final NaturalPersonRepository repository;

    public Mono<PageResponse<NaturalPerson>> listAllNaturals(Pageable pageRequest, NaturalPersonFilterRequest filterRequest) {

        return repository
                .listAllNaturals(pageRequest, filterRequest)
                .switchIfEmpty(Mono.error(new EmptyReturnException(GENERAL_WARNING_EMPTY)))
                .map(pageMapper::toPageResponse)
                .doOnError(errorResponse -> Mono.error(new Exception(errorResponse.getMessage())));
    }

    public Mono<NaturalPerson> findNaturalById(String id) {

        return repository
                .findById(id)
                .switchIfEmpty(Mono.error(new EmptyReturnException(GENERAL_WARNING_EMPTY)))
                .doOnError(errorResponse -> Mono.error(new Exception(errorResponse.getMessage())));
    }

    public Mono<NaturalPerson> findNaturalByCPF(String cpf) {

        return repository
                .findByCpf(cpf)
                .switchIfEmpty(Mono.error(new EmptyReturnException(GENERAL_WARNING_EMPTY)))
                .doOnError(errorResponse -> Mono.error(new Exception(errorResponse.getMessage())));
    }

    public Mono<Void> saveNatural(NaturalPerson naturalPerson, String newNaturalCPF) {

        return Mono.just(naturalPerson)
                .flatMap(self -> {
                    self.setCpf(newNaturalCPF);
                    return repository.findByCpf(self.getCpf());
                })
                .flatMap(alreadyExistentNatural -> Mono.error(new BadRequestException(messageHelper.getMessage(NATURAL_SAVE_ALREADY_EXISTS))))
                .switchIfEmpty(validateNewNatural(naturalPerson)
                        .publishOn(Schedulers.boundedElastic())
                        .map(newNatural -> repository.save(newNatural).subscribe())
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

        return findNaturalByCPF(cpf)
                .flatMap(natural -> fillEmptyFieldsIfHas(natural, naturalPerson))
                .publishOn(Schedulers.boundedElastic())
                .map(updatedNatural -> repository.save(updatedNatural).subscribe())
                .doOnSuccess(unused -> log.info("NaturalPerson was updated with success!"))
                .doOnError(errorResponse -> Mono.error(new Exception(errorResponse.getMessage())))
                .then();
    }
    private Mono<NaturalPerson> fillEmptyFieldsIfHas(NaturalPerson oldNatural, NaturalPerson newNatural) {

        return Mono
                .zip(Mono.just(oldNatural), Mono.just(newNatural))
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
            return null;
//        return findNaturalByCPF(cpf)
//                .map(self -> repository.deleteByCpf(self.getCpf()).subscribe())
//                .flatMap(unused -> legalPersonService.deletePartner(cpf))
//                .doOnSuccess(unused -> log.info("Natural with CPF: {}, was deleted with success!", cpf))
//                .then();
    }

}

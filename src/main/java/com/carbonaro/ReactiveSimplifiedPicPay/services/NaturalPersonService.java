package com.carbonaro.ReactiveSimplifiedPicPay.services;

import com.carbonaro.ReactiveSimplifiedPicPay.domain.mappers.IPersonMapper;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.responses.NaturalPersonResponse;
import com.carbonaro.ReactiveSimplifiedPicPay.repositories.NaturalPersonRepository;
import com.carbonaro.ReactiveSimplifiedPicPay.services.exceptions.EmptyException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@AllArgsConstructor
public class NaturalPersonService {

    private final NaturalPersonRepository repositoryNP;

    public Flux<NaturalPersonResponse> findAllNaturals() {

        return repositoryNP
                .findAll()
                .switchIfEmpty(Flux.error(new EmptyException()))
                .map(IPersonMapper.INSTANCE::toNaturalPersonResponse)
                .doOnError(errorResponse -> Flux.error(new Exception(errorResponse.getMessage())));
    }
}

package com.carbonaro.ReactiveSimplifiedPicPay.services;

import com.carbonaro.ReactiveSimplifiedPicPay.domain.mappers.IPersonMapper;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.responses.LegalPersonResponse;
import com.carbonaro.ReactiveSimplifiedPicPay.repositories.LegalPersonRepository;
import com.carbonaro.ReactiveSimplifiedPicPay.services.exceptions.EmptyException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@AllArgsConstructor
public class LegalPersonService {

    private final LegalPersonRepository repositoryLP;

    public Flux<LegalPersonResponse> findAllLegals() {

        return repositoryLP
                .findAll()
                .switchIfEmpty(Flux.error(new EmptyException()))
                .map(IPersonMapper.INSTANCE::toLegalPersonResponse)
                .doOnError(errorResponse -> Flux.error(new Exception(errorResponse.getMessage())));
    }
}

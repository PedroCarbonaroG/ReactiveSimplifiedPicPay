package com.carbonaro.ReactiveSimplifiedPicPay.api.impl;

import com.carbonaro.ReactiveSimplifiedPicPay.api.IPersonAPI;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.responses.LegalPersonResponse;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.responses.NaturalPersonResponse;
import com.carbonaro.ReactiveSimplifiedPicPay.services.LegalPersonService;
import com.carbonaro.ReactiveSimplifiedPicPay.services.NaturalPersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
@EnableReactiveMongoRepositories
public class IPersonImpl implements IPersonAPI {

    private NaturalPersonService naturalPersonService;
    private LegalPersonService legalPersonService;

    @Override
    public Flux<LegalPersonResponse> findAllLegals() {

        return legalPersonService
                .findAllLegals();
    }

    @Override
    public Flux<NaturalPersonResponse> findAllNaturals() {

        return naturalPersonService
                .findAllNaturals();
    }
}

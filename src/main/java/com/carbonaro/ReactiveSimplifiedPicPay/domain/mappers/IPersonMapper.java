package com.carbonaro.ReactiveSimplifiedPicPay.domain.mappers;

import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.LegalPerson;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.NaturalPerson;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.requests.person.LegalPersonRequest;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.requests.person.NaturalPersonRequest;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.responses.person.LegalPersonResponse;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.responses.person.NaturalPersonResponse;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.responses.transaction.LegalPersonTransactionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface IPersonMapper {

    IPersonMapper INSTANCE = Mappers.getMapper(IPersonMapper.class);

    NaturalPerson toNaturalPersonByRequest(NaturalPersonRequest naturalPersonRequest);
    NaturalPersonResponse toNaturalPersonResponse(NaturalPerson naturalPerson);

    LegalPerson toLegalPersonByRequest(LegalPersonRequest legalPersonRequest);
    LegalPersonResponse toLegalPersonResponse(LegalPerson legalPerson);
    LegalPersonTransactionResponse toLegalPersonTransactionResponse(LegalPerson legalPerson);

}

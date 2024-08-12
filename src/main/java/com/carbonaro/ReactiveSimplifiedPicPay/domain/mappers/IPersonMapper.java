package com.carbonaro.ReactiveSimplifiedPicPay.domain.mappers;

import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.LegalPerson;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.NaturalPerson;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.requests.LegalPersonRequest;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.requests.NaturalPersonRequest;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.responses.person.LegalPersonResponse;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.responses.person.NaturalPersonResponse;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.responses.transaction.LegalPersonTransactionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface IPersonMapper {

    IPersonMapper INSTANCE = Mappers.getMapper(IPersonMapper.class);

    LegalPersonResponse toLegalPersonResponse(LegalPerson legalPerson);
    NaturalPersonResponse toNaturalPersonResponse(NaturalPerson naturalPerson);
    LegalPersonTransactionResponse toLegalPersonTransactionResponse(LegalPerson legalPerson);
    LegalPerson toLegalPersonByRequest(LegalPersonRequest legalPersonRequest);
    NaturalPerson toNaturalPersonByRequest(NaturalPersonRequest naturalPersonRequest);

}

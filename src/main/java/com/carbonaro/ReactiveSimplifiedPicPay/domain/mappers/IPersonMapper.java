package com.carbonaro.ReactiveSimplifiedPicPay.domain.mappers;

import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.LegalPerson;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.NaturalPerson;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.responses.LegalPersonResponse;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.responses.NaturalPersonResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface IPersonMapper {

    IPersonMapper INSTANCE = Mappers.getMapper(IPersonMapper.class);

    LegalPersonResponse toLegalPersonResponse(LegalPerson person);

    NaturalPersonResponse toNaturalPersonResponse(NaturalPerson person);
}

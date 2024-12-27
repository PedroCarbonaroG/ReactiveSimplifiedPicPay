package com.carbonaro.ReactiveSimplifiedPicPay.domain.mappers;

import com.carbonaro.ReactiveSimplifiedPicPay.api.requests.LegalPersonRequest;
import com.carbonaro.ReactiveSimplifiedPicPay.api.requests.NaturalPersonRequest;
import com.carbonaro.ReactiveSimplifiedPicPay.api.responses.PageResponse;
import com.carbonaro.ReactiveSimplifiedPicPay.api.responses.person.LegalPersonResponse;
import com.carbonaro.ReactiveSimplifiedPicPay.api.responses.person.NaturalPersonResponse;
import com.carbonaro.ReactiveSimplifiedPicPay.api.responses.transaction.LegalPersonTransactionResponse;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.LegalPerson;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.NaturalPerson;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface IPersonMapper {

    IPersonMapper INSTANCE = Mappers.getMapper(IPersonMapper.class);

    LegalPerson toLegalPersonByRequest(LegalPersonRequest legalPersonRequest);
    LegalPersonResponse toLegalPersonResponse(LegalPerson legalPerson);
    LegalPersonTransactionResponse toLegalPersonTransactionResponse(LegalPerson legalPerson);

    NaturalPerson toNaturalPersonByRequest(NaturalPersonRequest naturalPersonRequest);
    NaturalPersonResponse toNaturalPersonResponse(NaturalPerson naturalPerson);

    @Mapping(source = "pageNaturals.pageable.pageNumber", target = "pageNumber")
    @Mapping(source = "pageNaturals.pageable.pageSize", target = "pageSize")
    PageResponse<NaturalPersonResponse> toPageResponseNaturalPersonResponse(Page<NaturalPerson> pageNaturals);

    @Mapping(source = "pageLegals.pageable.pageNumber", target = "pageNumber")
    @Mapping(source = "pageLegals.pageable.pageSize", target = "pageSize")
    PageResponse<LegalPersonResponse> toPageResponseLegalPersonResponse(Page<LegalPerson> pageLegals);

}

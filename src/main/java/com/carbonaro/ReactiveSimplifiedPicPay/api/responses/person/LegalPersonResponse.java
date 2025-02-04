package com.carbonaro.ReactiveSimplifiedPicPay.api.responses.person;

import com.carbonaro.ReactiveSimplifiedPicPay.domain.enums.CompanySizeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LegalPersonResponse extends PersonResponse {

    @Schema(description = "Demonstration of the unique LegalPerson CNPJ.")
    private String cnpj;

    @Schema(description = "Demonstration of the company size")
    private CompanySizeEnum companySize;

    @Schema(description = "Number of employees in the company")
    private int employeesNumber;

    @Schema(description = "Demonstration of the company partners")
    List<NaturalPersonResponse> partners;

}


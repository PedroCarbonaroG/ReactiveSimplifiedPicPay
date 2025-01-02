package com.carbonaro.ReactiveSimplifiedPicPay.api.requests.person;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LegalPersonRequest extends PersonRequest {

    @Schema(description = "Number of employees in the company")
    private String cnpj;

    @Schema(description = "Number of employees in the company")
    private int employeesNumber;

}

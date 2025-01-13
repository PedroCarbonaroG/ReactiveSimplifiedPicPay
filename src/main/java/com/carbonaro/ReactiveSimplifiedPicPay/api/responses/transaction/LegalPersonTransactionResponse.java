package com.carbonaro.ReactiveSimplifiedPicPay.api.responses.transaction;

import com.carbonaro.ReactiveSimplifiedPicPay.api.responses.person.PersonResponse;
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
public class LegalPersonTransactionResponse extends PersonResponse {

    @Schema(description = "Demonstration of the unique LegalPerson CNPJ.")
    private String cnpj;
}

package com.carbonaro.ReactiveSimplifiedPicPay.api.responses.transaction;

import com.carbonaro.ReactiveSimplifiedPicPay.api.responses.person.PersonResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class LegalPersonTransactionResponse extends PersonResponse {

    @Schema(description = "Demonstration of the unique LegalPerson CNPJ.")
    private String cnpj;
}

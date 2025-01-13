package com.carbonaro.ReactiveSimplifiedPicPay.api.responses.person;

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
public class NaturalPersonResponse extends PersonResponse {

    @Schema(description = "Unique demonstration of the NaturalPerson CPF.")
    private String cpf;

}
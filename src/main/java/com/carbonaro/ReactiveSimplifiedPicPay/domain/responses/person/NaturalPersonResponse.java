package com.carbonaro.ReactiveSimplifiedPicPay.domain.responses.person;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class NaturalPersonResponse extends PersonResponse {

    @Schema(description = "Unique demonstration of the NaturalPerson CPF.")
    private String cpf;

}
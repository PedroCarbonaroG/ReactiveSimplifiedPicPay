package com.carbonaro.ReactiveSimplifiedPicPay.domain.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NaturalPersonResponse {

    @Schema(description = "Demonstração do CPF da pessoa física")
    private String cpf;

    @Schema(description = "Demonstração do nome completo da pessoa física")
    private String name;

    @Schema(description = "Demonstração do email completo da pessoa física")
    private String email;

    @Schema(description = "Demonstração do endereço completo da pessoa física")
    private String address;
}
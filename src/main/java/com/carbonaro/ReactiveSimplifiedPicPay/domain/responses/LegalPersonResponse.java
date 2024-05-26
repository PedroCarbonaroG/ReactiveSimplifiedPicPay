package com.carbonaro.ReactiveSimplifiedPicPay.domain.responses;

import com.carbonaro.ReactiveSimplifiedPicPay.domain.enums.CompanySizeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LegalPersonResponse {

    @Schema(description = "Demonstração do CNPJ da pessoa jurídica")
    private String cnpj;

    @Schema(description = "Demonstração do nome completo da pessoa jurídica")
    private String name;

    @Schema(description = "Demonstração do email completo da pessoa jurídica")
    private String email;

    @Schema(description = "Demonstração do endereço completo da pessoa jurídica")
    private String address;

    @Schema(description = "Demonstração do tamanho da empresa da pessoa jurídica")
    private CompanySizeEnum companySize;

    @Schema(description = "Demostração dos sócios da empresa da pessoa jurídica")
    List<NaturalPersonResponse> partners;
}

package com.carbonaro.ReactiveSimplifiedPicPay.domain.entities;

import com.carbonaro.ReactiveSimplifiedPicPay.domain.enums.CompanySizeEnum;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.responses.NaturalPersonResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Document(value = "LegalPerson")
public class LegalPerson extends Person {

    @Schema(description = "LegalPerson unique CNPJ")
    @Indexed(unique = true)
    private String cnpj;

    @Schema(description = "Saldo bancário da pessoa jurídica")
    private BigDecimal balance;

    @Schema(description = "Faturamento mensal da empresa")
    private BigDecimal monthlyBilling;

    @Schema(description = "Faturamento anual da empresa")
    private BigDecimal annualBilling;

    @Schema(description = "Configuração de tamanho da empresa baseado no faturamento anual")
    private CompanySizeEnum companySize;

    @Schema(description = "Sócios da empresa")
    List<NaturalPersonResponse> partners;

    public void setCompanySize() {
        setInternalCompanySize();
    }

    private void setInternalCompanySize() {
        if (annualBilling.doubleValue() <= 360000) { this.companySize = CompanySizeEnum.MICROBUSINESS; }
        else if (annualBilling.doubleValue() <= 4800000) { this.companySize = CompanySizeEnum.SMALL_ENTERPRISE; }
        else if (annualBilling.doubleValue() <= 300000000) { this.companySize = CompanySizeEnum.MEDIUM_ENTERPRISE; }
        else { this.companySize = CompanySizeEnum.LARGE_ENTERPRISE; }
    }
}

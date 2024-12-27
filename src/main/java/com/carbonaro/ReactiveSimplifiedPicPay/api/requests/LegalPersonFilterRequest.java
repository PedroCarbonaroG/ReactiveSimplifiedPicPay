package com.carbonaro.ReactiveSimplifiedPicPay.api.requests;

import com.carbonaro.ReactiveSimplifiedPicPay.domain.enums.CompanySizeEnum;
import java.math.BigDecimal;
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
public class LegalPersonFilterRequest extends PagedRequest {

    private String cnpj;
    private String name;
    private String address;
    private CompanySizeEnum companySize;
    private Integer employeesNumber;
    private BigDecimal initialMonthlyBilling;
    private BigDecimal finalMonthlyBilling;
    private BigDecimal initialAnnualBilling;
    private BigDecimal finalAnnualBilling;

}

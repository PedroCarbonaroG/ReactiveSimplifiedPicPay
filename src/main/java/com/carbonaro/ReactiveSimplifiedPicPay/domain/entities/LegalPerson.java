package com.carbonaro.ReactiveSimplifiedPicPay.domain.entities;

import com.carbonaro.ReactiveSimplifiedPicPay.domain.enums.CompanySizeEnum;
import jdk.jfr.Description;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Document(value = "LegalPerson")
@EqualsAndHashCode(callSuper = true)
public class LegalPerson extends Person {

    @Indexed(unique = true)
    @Description("Unique LegalPerson CNPJ.")
    private String cnpj;

    @Description("Company unique bank balance.")
    private BigDecimal balance;

    @Description("Company monthly income.")
    private BigDecimal monthlyBilling;

    @Description("Company annual income.")
    private BigDecimal annualBilling;

    @Description("Company size based on annual billing")
    private CompanySizeEnum companySize;

    @Description("List of all company partners")
    List<NaturalPerson> partners;

    @Description("Number of employees in the company")
    private Integer employeesNumber;

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

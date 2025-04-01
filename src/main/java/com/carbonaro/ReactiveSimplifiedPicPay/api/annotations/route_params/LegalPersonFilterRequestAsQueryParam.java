package com.carbonaro.ReactiveSimplifiedPicPay.api.annotations.route_params;

import com.carbonaro.ReactiveSimplifiedPicPay.domain.enums.CompanySizeEnum;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.math.BigDecimal;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Parameter(
        in = ParameterIn.QUERY,
        name = "cnpj",
        description = "Company CNPJ",
        schema = @Schema(implementation = String.class))
@Parameter(
        in = ParameterIn.QUERY,
        name = "name",
        description = "Company name",
        schema = @Schema(implementation = String.class))
@Parameter(
        in = ParameterIn.QUERY,
        name = "address",
        description = "Company address",
        schema = @Schema(implementation = String.class))
@Parameter(
        in = ParameterIn.QUERY,
        name = "companySize",
        description = "Company size",
        schema = @Schema(implementation = CompanySizeEnum.class))
@Parameter(
        in = ParameterIn.QUERY,
        name = "initialMonthlyBilling",
        description = "Company initial monthly billing to filter",
        schema = @Schema(implementation = BigDecimal.class))
@Parameter(
        in = ParameterIn.QUERY,
        name = "finalMonthlyBilling",
        description = "Company final monthly billing to filter",
        schema = @Schema(implementation = BigDecimal.class))
@Parameter(
        in = ParameterIn.QUERY,
        name = "initialAnnualBilling",
        description = "Company initial annual billing to filter",
        schema = @Schema(implementation = BigDecimal.class))
@Parameter(
        in = ParameterIn.QUERY,
        name = "finalAnnualBilling",
        description = "Company final annual billing to filter",
        schema = @Schema(implementation = BigDecimal.class))
public @interface LegalPersonFilterRequestAsQueryParam {
}

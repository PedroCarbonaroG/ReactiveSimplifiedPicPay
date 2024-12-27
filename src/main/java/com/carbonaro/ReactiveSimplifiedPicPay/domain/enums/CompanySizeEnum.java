package com.carbonaro.ReactiveSimplifiedPicPay.domain.enums;

import io.swagger.v3.oas.annotations.media.Schema;

public enum CompanySizeEnum {

    @Schema(description = "Companies with revenues less than or equal to R$360K - ANNUAL BILLING.")
    MICROBUSINESS,

    @Schema(description = "Companies with revenues higher than R$360K and less or equal to R$4.8M - ANNUAL BILLING")
    SMALL_ENTERPRISE,

    @Schema(description = "Companies with revenues higher than 4.8M and less or equal to R$300M - ANNUAL BILLING")
    MEDIUM_ENTERPRISE,

    @Schema(description = "Companies with revenues higher than R$300M - ANNUAL BILLING")
    LARGE_ENTERPRISE;

}
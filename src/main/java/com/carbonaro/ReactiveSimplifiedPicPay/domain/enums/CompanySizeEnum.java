package com.carbonaro.ReactiveSimplifiedPicPay.domain.enums;

import io.swagger.v3.oas.annotations.media.Schema;

public enum CompanySizeEnum {

    @Schema(description = "Empresas com faturamento menor ou igual a R$ 360 mil. - FATURAMENTO ANUAL")
    MICROBUSINESS,

    @Schema(description = "Empresas com faturamento maior que R$ 360 mil e menor ou igual a R$ 4,8 milhões. - FATURAMENTO ANUAL")
    SMALL_ENTERPRISE,

    @Schema(description = "Empresas com faturamento maior que R$ 4,8 milhões e menor ou igual a R$ 300 milhões. - FATURAMENTO ANUAL")
    MEDIUM_ENTERPRISE,

    @Schema(description = "Empresas com faturamento maior que R$ 300 milhões. - FATURAMENTO ANUAL")
    LARGE_ENTERPRISE;
}
package com.carbonaro.ReactiveSimplifiedPicPay.domain.responses.person;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class PersonResponse {

    @Schema(description = "Person full name.")
    protected String name;

    @Schema(description = "Person email.")
    private String email;

    @Schema(description = "Person full address.")
    private String address;

}

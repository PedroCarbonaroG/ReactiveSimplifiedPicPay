package com.carbonaro.ReactiveSimplifiedPicPay.api.responses.person;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
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
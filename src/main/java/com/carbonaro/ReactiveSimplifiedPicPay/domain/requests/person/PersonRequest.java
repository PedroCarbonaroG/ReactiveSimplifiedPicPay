package com.carbonaro.ReactiveSimplifiedPicPay.domain.requests.person;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class PersonRequest {

    @Schema(description = "Person full name.")
    protected String name;

    @Schema(description = "Person email.")
    private String email;

    @Schema(description = "Person full address.")
    private String address;

    @Schema(description = "Person access password.")
    private String password;

}

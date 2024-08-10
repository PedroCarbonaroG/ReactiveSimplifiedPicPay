package com.carbonaro.ReactiveSimplifiedPicPay.domain.requests;

import jdk.jfr.Description;
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

    @Description("Person full name.")
    protected String name;

    @Description("Person email.")
    private String email;

    @Description("Person full address.")
    private String address;

    @Description("Person access password.")
    private String password;
}

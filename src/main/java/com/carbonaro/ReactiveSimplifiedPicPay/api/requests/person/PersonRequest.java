package com.carbonaro.ReactiveSimplifiedPicPay.api.requests.person;

import jdk.jfr.Description;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
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

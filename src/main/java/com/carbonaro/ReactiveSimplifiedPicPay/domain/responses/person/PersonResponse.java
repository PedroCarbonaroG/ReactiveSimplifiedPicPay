package com.carbonaro.ReactiveSimplifiedPicPay.domain.responses.person;

import jdk.jfr.Description;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class PersonResponse {

    @Description("Person full name.")
    protected String name;

    @Description("Person email.")
    private String email;

    @Description("Person full address.")
    private String address;

}

package com.carbonaro.ReactiveSimplifiedPicPay.domain.entities;

import jdk.jfr.Description;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class Person {

    @Id
    @Description("Unique Person identifier.")
    private String id;

    @Description("Person full name.")
    protected String name;

    @Description("Person email.")
    private String email;

    @Description("Person full address.")
    private String address;

    @Description("Person access password.")
    private String password;

}

package com.carbonaro.ReactiveSimplifiedPicPay.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SystemUserEnum {

    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");

    private final String value;

}

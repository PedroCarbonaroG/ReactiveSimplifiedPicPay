package com.carbonaro.ReactiveSimplifiedPicPay.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileTypeEnum {

    PDF("pdf"),
    EXCEL("xlsx");

    private final String value;

}

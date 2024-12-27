package com.carbonaro.ReactiveSimplifiedPicPay.api.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Sort;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class PagedRequest {

    private int page;
    private int size;
    private Sort sort;

}

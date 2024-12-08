package com.carbonaro.ReactiveSimplifiedPicPay.api.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {

    private int pageNumber;
    private int pageSize;
    private int totalPages;
    private long totalElements;
    private int numberOfElements;
    private List<T> content;

}

package com.carbonaro.ReactiveSimplifiedPicPay.domain.mappers;

import com.carbonaro.ReactiveSimplifiedPicPay.api.responses.PageResponse;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class PageMapper<T> {

    public PageResponse<T> toPageResponse(Page<T> page) {

        PageResponse<T> pageResponse = new PageResponse<>();
        pageResponse.setPageNumber(page.getPageable().getPageNumber());
        pageResponse.setPageSize(page.getPageable().getPageSize());
        pageResponse.setTotalPages(page.getTotalPages());
        pageResponse.setTotalElements(page.getTotalElements());
        pageResponse.setNumberOfElements(page.getNumberOfElements());
        pageResponse.setContent(page.getContent());

        return pageResponse;
    }
}

package com.carbonaro.ReactiveSimplifiedPicPay.repositories;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Mono;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.stream.Stream;

public class BaseRepository {

    @Autowired
    private ReactiveMongoTemplate mongoTemplate;

    protected <T> Mono<Page<T>> toPage(Query query, Pageable page, Class<T> clazz) {

        return mongoTemplate.count(query, clazz)
                .flatMap(totalRecords -> mongoTemplate
                        .find(query.with(page), clazz)
                        .collectList()
                        .map(list -> new PageImpl<>(list, page, totalRecords)));
    }

    protected <T> void addParam(T param, String fieldName, Query query) {
        if (Objects.nonNull(param)) query.addCriteria(Criteria.where(fieldName).is(param));
    }

    protected void addParamBetweenDates(LocalDate initialDate, LocalDate finalDate, String fieldName, Query query) {
        if (checkValidParams(initialDate, finalDate, fieldName, query)) {
            query.addCriteria(Criteria.where(fieldName).gte(initialDate).lte(finalDate));
        }
    }

    protected void addParamValues(BigDecimal initialValue, BigDecimal finalValue, String fieldName, Query query) {
        if (checkValidParams(initialValue, finalValue, fieldName, query)) {
            query.addCriteria(Criteria.where(fieldName).gte(initialValue).lte(finalValue));
        }
    }

    private boolean checkValidParams(Object... params) {
        return Stream.of(params).allMatch(Objects::nonNull);
    }

    @SneakyThrows
    protected <T> T copyPropertiesForNullFields(T source, T target) {

        var fields = Stream.concat(
                Stream.of(source.getClass().getDeclaredFields()),
                Stream.of(source.getClass().getSuperclass().getDeclaredFields())
        ).toList();

        for (Field field : fields) {

            field.setAccessible(true);
            if (Objects.isNull(field.get(target))) {
                field.set(target, field.get(source));
            }
        }

        return target;
    }

}
